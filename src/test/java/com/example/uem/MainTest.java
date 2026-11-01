package com.example.uem;

import com.example.uem.model.Article;
import com.example.uem.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {
    @Test
    @DisplayName("loadOrders: carga los pedidos de order.json con id y artículos")
    void testLoadOrders_ok() throws IOException {
        List<Order> orders = Main.loadOrders("/order.json");

        assertFalse(orders.isEmpty());
        for (Order order : orders) {
            assertNotNull(order.getId());
            assertFalse(order.getArticulos().isEmpty());
        }
    }

    @Test
    @DisplayName("loadOrders: los artículos se rellenan con sus datos (no quedan a 0)")
    void testLoadOrders_articulosConDatos() throws IOException {
        List<Order> orders = Main.loadOrders("/order.json");

        for (Order order : orders) {
            for (Article art : order.getArticulos()) {
                assertNotNull(art.getNombre());
                assertTrue(art.getCantidad() > 0);
                assertTrue(art.getPrecio() > 0);
            }
        }
    }

    @Test
    @DisplayName("loadOrders: un recurso que no existe lanza IOException")
    void testLoadOrders_noExiste() {
        assertThrows(IOException.class, () -> {
            Main.loadOrders("/no-existe.json");
        });
    }
}
