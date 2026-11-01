package com.example.uem.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.uem.model.Order;
import com.example.uem.model.Searcher;
import com.example.uem.view.OrderView;

public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    OrderView view;
    List<Order> orders;
    Searcher searcher;

    public OrderController(OrderView view, List<Order> orders) {
        this.view = view;
        this.orders = orders;
        this.searcher = new Searcher();

        this.view.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOrder();
            }
        });
    }

    private void searchOrder() {
        String id = view.getSearchId();
        log.debug("Searching order: {}", id);

        Order order = searcher.searchOrderById(id, orders);
        if (order != null) {
            log.info("Order found: {}", id);
            view.displayOrder(order, 1.0);
        } else {
            log.info("Order not found: {}", id);
            view.displayMessage("Order not found.");
        }
    }
}
