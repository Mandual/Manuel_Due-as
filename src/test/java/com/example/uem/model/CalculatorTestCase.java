package com.example.uem.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTestCase {
    private final Calculator calculator = new Calculator();

    @Test
    @DisplayName("multiply: multiplicación normal")
    void testMultiply_normal() {
        int resultado = calculator.multiply(2, 3);

        assertEquals(6, resultado);
    }

    @Test
    @DisplayName("multiply: multiplicación con cero")
    void testMultiply_withZero() {
        int resultado = calculator.multiply(5, 0);

        assertEquals(0, resultado);
    }

    @Test
    @DisplayName("multiply: multiplicación con números negativos")
    void testMultiply_withNegatives() {
        int resultado = calculator.multiply(-3, 4);

        assertEquals(-12, resultado);
    }

    @Test
    @DisplayName("multiply: sobrecarga double * int, ej. precio por cantidad")
    void testMultiply_doubleAndInt() {
        double resultado = calculator.multiply(10.5, 3);

        assertEquals(31.5, resultado, 0.0001);
    }

    @Test
    @DisplayName("concat: concatenación de dos cadenas normales")
    void testConcat_normal() {
        String resultado = calculator.concat("hola", "mundo");

        assertEquals("holamundo", resultado);
    }

    @Test
    @DisplayName("concat: si un parámetro es null, devuelve 'empty'")
    void testConcat_withNull() {
        String resultado1 = calculator.concat(null, "mundo");
        String resultado2 = calculator.concat("hola", null);

        assertEquals("empty", resultado1);
        assertEquals("empty", resultado2);
    }

    @Test
    @DisplayName("sum: suma normal")
    void testSum_normal() {
        double resultado = calculator.sum(2.5, 3.5);

        assertEquals(6.0, resultado, 0.0001);
    }

    @Test
    @DisplayName("sum: suma con valores negativos")
    void testSum_withNegatives() {
        double resultado = calculator.sum(-5.0, 2.0);

        assertEquals(-3.0, resultado, 0.0001);
    }

    @Test
    @DisplayName("discount: se aplica correctamente un descuento válido")
    void testDiscount_validPercent() {
        double resultado = calculator.discount(100.0, 20.0);

        assertEquals(80.0, resultado, 0.0001);
    }

    @Test
    @DisplayName("discount: descuento del 0% devuelve el importe original")
    void testDiscount_zeroPercent() {
        double resultado = calculator.discount(100.0, 0.0);

        assertEquals(100.0, resultado, 0.0001);
    }

    @Test
    @DisplayName("discount: descuento del 100% devuelve 0")
    void testDiscount_hundredPercent() {
        double resultado = calculator.discount(100.0, 100.0);

        assertEquals(0.0, resultado, 0.0001);
    }

    @Test
    @DisplayName("discount: porcentaje negativo lanza IllegalArgumentException")
    void testDiscount_negativePercent_throws() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.discount(100.0, -10.0);
        });
    }

    @Test
    @DisplayName("discount: porcentaje mayor que 100 lanza IllegalArgumentException")
    void testDiscount_percentAbove100_throws() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.discount(100.0, 150.0);
        });
    }

    @Test
    @DisplayName("calculateTotal: una lista de importes devuelve la suma correcta")
    void testCalculateTotal_normalList() {
        List<Double> importes = Arrays.asList(10.0, 20.5, 5.5);

        double resultado = calculator.calculateTotal(importes);

        assertEquals(36.0, resultado, 0.0001);
    }

    @Test
    @DisplayName("calculateTotal: una lista vacía devuelve 0.0")
    void testCalculateTotal_emptyList() {
        List<Double> importes = new ArrayList<>();

        double resultado = calculator.calculateTotal(importes);

        assertEquals(0.0, resultado, 0.0001);
    }
}
