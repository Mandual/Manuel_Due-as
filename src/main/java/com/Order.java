package com;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private String id;
    @SerializedName("articles")
    private List<Article> articulos;

    private final Calculator calculator = new Calculator();

    // Constructor vacío
    public Order() {
        this.articulos = new ArrayList<>();
    }

    public Order(String id, List<Article> articulos) {
        this.id = id;
        this.articulos = articulos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Article> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<Article> articulos) {
        this.articulos = articulos;
    }

    public double getBruto() {
        List<Double> importes = new ArrayList<>();
        for (Article articulo : articulos) {
            importes.add(articulo.getBruto());
        }
        return calculator.calculateTotal(importes);
    }

    public double getDescuento() {
        List<Double> importes = new ArrayList<>();
        for (Article articulo : articulos) {
            importes.add(articulo.getDescuento());
        }
        return calculator.calculateTotal(importes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order{id='").append(id).append('\'');
        sb.append(", articulos=[");
        for (int i = 0; i < articulos.size(); i++) {
            sb.append(articulos.get(i).toString());
            if (i < articulos.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]}");
        return sb.toString();
    }
}
