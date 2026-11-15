package com.example.uem.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExchangeRateServiceTest {

    private HttpServer startServer(final int status, final String body) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/rate", new HttpHandler() {
            @Override
            public void handle(HttpExchange ex) throws IOException {
                byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
                ex.sendResponseHeaders(status, bytes.length);
                OutputStream os = ex.getResponseBody();
                os.write(bytes);
                os.close();
            }
        });
        server.start();
        return server;
    }

    private String urlOf(HttpServer server) {
        return "http://127.0.0.1:" + server.getAddress().getPort() + "/rate";
    }

    @Test
    @DisplayName("parseRate: formato de la API v2")
    void testParseRate_v2() throws IOException {
        String json = "{\"date\":\"2026-07-17\",\"base\":\"EUR\",\"quote\":\"USD\",\"rate\":1.0854}";

        assertEquals(1.0854, ExchangeRateService.parseRate(json), 0.0001);
    }

    @Test
    @DisplayName("parseRate: formato de la API v1")
    void testParseRate_v1() throws IOException {
        String json = "{\"amount\":1.0,\"base\":\"EUR\",\"date\":\"2026-07-22\",\"rates\":{\"USD\":1.0912}}";

        assertEquals(1.0912, ExchangeRateService.parseRate(json), 0.0001);
    }

    @Test
    @DisplayName("parseRate: JSON inválido lanza IOException")
    void testParseRate_invalidJson() {
        assertThrows(IOException.class, () -> {
            ExchangeRateService.parseRate("esto no es json");
        });
    }

    @Test
    @DisplayName("parseRate: JSON sin tipo de cambio lanza IOException")
    void testParseRate_withoutRate() {
        assertThrows(IOException.class, () -> {
            ExchangeRateService.parseRate("{\"base\":\"EUR\"}");
        });
    }

    @Test
    @DisplayName("parseRate: tipo de cambio cero o negativo lanza IOException")
    void testParseRate_invalidValue() {
        assertThrows(IOException.class, () -> {
            ExchangeRateService.parseRate("{\"rate\":0}");
        });
        assertThrows(IOException.class, () -> {
            ExchangeRateService.parseRate("{\"rate\":-1.2}");
        });
    }

    @Test
    @DisplayName("getEurUsdRate: obtiene el tipo de cambio de un servidor HTTP")
    void testGetEurUsdRate_ok() throws IOException {
        HttpServer server = startServer(200, "{\"rate\":1.2345}");
        try {
            ExchangeRateService service = new ExchangeRateService(urlOf(server));

            assertEquals(1.2345, service.getEurUsdRate(), 0.0001);
        } finally {
            server.stop(0);
        }
    }

    @Test
    @DisplayName("getEurUsdRate: si la primera URL falla usa la siguiente")
    void testGetEurUsdRate_fallback() throws IOException {
        HttpServer broken = startServer(500, "error");
        HttpServer good = startServer(200, "{\"rates\":{\"USD\":1.1}}");
        try {
            ExchangeRateService service = new ExchangeRateService(urlOf(broken), urlOf(good));

            assertEquals(1.1, service.getEurUsdRate(), 0.0001);
        } finally {
            broken.stop(0);
            good.stop(0);
        }
    }

    @Test
    @DisplayName("getEurUsdRate: si todas las URLs fallan lanza IOException")
    void testGetEurUsdRate_allFail() throws IOException {
        HttpServer broken = startServer(500, "error");
        try {
            ExchangeRateService service = new ExchangeRateService(urlOf(broken));

            assertThrows(IOException.class, () -> {
                service.getEurUsdRate();
            });
        } finally {
            broken.stop(0);
        }
    }
}
