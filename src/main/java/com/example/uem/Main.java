package com.example.uem;

import com.example.uem.controller.OrderController;
import com.example.uem.model.Order;
import com.example.uem.view.OrderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static List<Order> loadOrders(String resource) throws IOException {
        InputStream is = Main.class.getResourceAsStream(resource);
        if (is == null) {
            throw new IOException("No se encuentra el recurso: " + resource);
        }
        try {
            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            Type type = new TypeToken<List<Order>>() { }.getType();
            List<Order> orders = new Gson().fromJson(reader, type);
            if (orders == null) {
                orders = new ArrayList<>();
            }
            return orders;
        } finally {
            is.close();
        }
    }

    public static void main(String[] args) {
        try {
            List<Order> orders = loadOrders("/order.json");
            for (Order order : orders) {
                log.debug("Loaded order: {}", order.getId());
            }
            log.debug("Total orders loaded: {}", orders.size());

            OrderView view = new OrderView();
            new OrderController(view, orders);
        } catch (IOException e) {
            log.error("Error leyendo order.json", e);
        }
    }
}
