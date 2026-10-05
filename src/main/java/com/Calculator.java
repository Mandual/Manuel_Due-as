package com;

import java.util.List;

public class Calculator {
    public final static String EMPTY = "empty";

    public int multiply(int a, int b) {
        return a * b;
    }

    public double multiply(double a, int b) {
        return a * b;
    }

    public String concat(String a, String b) {
        if (b != null && a != null) {
            return a + b;
        }
        return EMPTY;
    }

    public double sum(double a, double b) {
        return a + b;
    }

    public double discount(double amount, double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        return amount - (amount * percent / 100.0);
    }

    public double calculateTotal(List<Double> amounts) {
        return amounts.stream().mapToDouble(Double::doubleValue).sum();
    }
}