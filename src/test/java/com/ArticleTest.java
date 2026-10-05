package com;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleTest {


    @Test
    @DisplayName("getBruto: cantidad * precio para un caso normal")
    void testGetBruto_normal() {
        Article articulo = new Article("camiseta", 3, 10.0, 0.0);

        double resultado = articulo.getBruto();

        assertEquals(30.0, resultado, 0.0001);
    }

    @Test
    @DisplayName("getBruto: con cantidad 1 devuelve el precio tal cual")
    void testGetBruto_cantidadUno() {
        Article articulo = new Article("libro", 1, 15.5, 0.0);

        double resultado = articulo.getBruto();

        assertEquals(15.5, resultado, 0.0001);
    }

    @Test
    @DisplayName("getBruto: con cantidad 0 devuelve 0.0")
    void testGetBruto_cantidadCero() {
        Article articulo = new Article("taza", 0, 8.0, 0.0);

        double resultado = articulo.getBruto();

        assertEquals(0.0, resultado, 0.0001);
    }


    @Test
    @DisplayName("getDescuento: aplica correctamente el descuento al importe bruto")
    void testGetDescuento_normal() {
        // bruto = 4 * 25.0 = 100.0; con 20% de descuento -> 80.0
        Article articulo = new Article("zapatillas", 4, 25.0, 20.0);

        double resultado = articulo.getDescuento();

        assertEquals(80.0, resultado, 0.0001);
    }

    @Test
    @DisplayName("getDescuento: con descuento 0% coincide con el importe bruto")
    void testGetDescuento_sinDescuento() {
        Article articulo = new Article("gorra", 2, 12.0, 0.0);

        double resultado = articulo.getDescuento();

        assertEquals(articulo.getBruto(), resultado, 0.0001);
    }

    @Test
    @DisplayName("getDescuento: con descuento 100% el importe es 0")
    void testGetDescuento_descuentoTotal() {
        Article articulo = new Article("bufanda", 5, 9.0, 100.0);

        double resultado = articulo.getDescuento();

        assertEquals(0.0, resultado, 0.0001);
    }

    @Test
    @DisplayName("getDescuento: porcentaje de descuento inválido lanza IllegalArgumentException")
    void testGetDescuento_descuentoInvalido_throws() {
        Article articulo = new Article("mochila", 1, 40.0, 150.0);

        assertThrows(IllegalArgumentException.class, () -> {
            articulo.getDescuento();
        });
    }
}
