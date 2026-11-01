package com.example.uem.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {
    @Test
    @DisplayName("getBruto: suma el importe bruto de todos los artículos")
    void testGetBruto_normal() {
        Article a1 = new Article("camiseta", 2, 10.0, 0.0);   // bruto 20.0
        Article a2 = new Article("pantalon", 1, 30.0, 0.0);   // bruto 30.0
        List<Article> articulos = Arrays.asList(a1, a2);
        Order pedido = new Order("PED-001", articulos);

        double resultado = pedido.getBruto();

        assertEquals(50.0, resultado, 0.0001);
    }

    @Test
    @DisplayName("getBruto: un pedido sin artículos devuelve 0.0")
    void testGetBruto_pedidoVacio() {
        Order pedido = new Order("PED-002", new ArrayList<>());

        double resultado = pedido.getBruto();

        assertEquals(0.0, resultado, 0.0001);
    }

    @Test
    @DisplayName("getDescuento: suma el importe con descuento de todos los artículos")
    void testGetDescuento_normal() {
        Article a1 = new Article("camiseta", 2, 10.0, 50.0);  // bruto 20.0 -> 10.0
        Article a2 = new Article("pantalon", 1, 30.0, 0.0);   // bruto 30.0 -> 30.0
        List<Article> articulos = Arrays.asList(a1, a2);
        Order pedido = new Order("PED-003", articulos);

        double resultado = pedido.getDescuento();

        assertEquals(40.0, resultado, 0.0001);
    }

    @Test
    @DisplayName("getDescuento: sin descuentos coincide con el total bruto")
    void testGetDescuento_sinDescuentos() {
        Article a1 = new Article("taza", 3, 5.0, 0.0);
        Article a2 = new Article("plato", 2, 7.5, 0.0);
        List<Article> articulos = Arrays.asList(a1, a2);
        Order pedido = new Order("PED-004", articulos);

        assertEquals(pedido.getBruto(), pedido.getDescuento(), 0.0001);
    }

    @Test
    @DisplayName("getDescuento: un pedido sin artículos devuelve 0.0")
    void testGetDescuento_pedidoVacio() {
        Order pedido = new Order("PED-005", new ArrayList<>());

        double resultado = pedido.getDescuento();

        assertEquals(0.0, resultado, 0.0001);
    }
}
