package com.example.uem.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Obtiene el tipo de cambio EUR/USD llamando a un servicio externo.
 *
 * Solución elegida: API pública Frankfurter (https://frankfurter.dev), gratuita y sin clave.
 * Publica los tipos de referencia de bancos centrales (entre ellos el BCE), que se actualizan
 * una o varias veces al día. Por eso el tipo de cambio no se guarda como constante: se consulta
 * cada vez que se muestra un pedido.
 *
 * Cómo funciona:
 * - Se hace una petición HTTP GET con java.net.http.HttpClient (incluido en el JDK, sin dependencias).
 * - Se prueba primero la API v2 (/v2/rate/EUR/USD, respuesta {"rate": 1.08...}) y, si falla,
 *   la v1 (/v1/latest?base=EUR&symbols=USD, respuesta {"rates": {"USD": 1.08...}}).
 * - La respuesta JSON se lee con Gson y se devuelve cuántos dólares vale 1 euro.
 * - Si el servicio no responde (sin internet, error HTTP, JSON raro) se lanza IOException y la
 *   aplicación muestra solo los euros, sin inventarse un valor.
 *
 * Las llamadas de red las lanza OrderController en un SwingWorker para no congelar la ventana.
 */
public class ExchangeRateService {

    private static final String URL_V2 = "https://api.frankfurter.dev/v2/rate/EUR/USD";
    private static final String URL_V1 = "https://api.frankfurter.dev/v1/latest?base=EUR&symbols=USD";

    private final String[] urls;
    private final HttpClient client;

    public ExchangeRateService() {
        this(URL_V2, URL_V1);
    }

    public ExchangeRateService(String... urls) {
        this.urls = urls;
        this.client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
    }

    public double getEurUsdRate() throws IOException {
        IOException lastError = new IOException("No hay ninguna URL configurada");
        for (String url : urls) {
            try {
                return fetchRate(url);
            } catch (IOException e) {
                lastError = e;
            }
        }
        throw lastError;
    }

    private double fetchRate(String url) throws IOException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(5))
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Respuesta HTTP " + response.statusCode() + " de " + url);
            }
            return parseRate(response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Consulta interrumpida", e);
        }
    }

    static double parseRate(String json) throws IOException {
        try {
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            double rate = 0;
            if (obj.has("rate")) {
                rate = obj.get("rate").getAsDouble();
            } else if (obj.has("rates") && obj.getAsJsonObject("rates").has("USD")) {
                rate = obj.getAsJsonObject("rates").get("USD").getAsDouble();
            }
            if (rate <= 0) {
                throw new IOException("La respuesta no contiene un tipo de cambio válido");
            }
            return rate;
        } catch (RuntimeException e) {
            throw new IOException("Respuesta con formato inesperado", e);
        }
    }
}
