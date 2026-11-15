package com.example.uem.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.uem.model.Order;
import com.example.uem.model.Searcher;
import com.example.uem.service.ExchangeRateService;
import com.example.uem.view.OrderView;

public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    OrderView view;
    List<Order> orders;
    Searcher searcher;
    ExchangeRateService rateService;

    public OrderController(OrderView view, List<Order> orders) {
        this(view, orders, new ExchangeRateService());
    }

    public OrderController(OrderView view, List<Order> orders, ExchangeRateService rateService) {
        this.view = view;
        this.orders = orders;
        this.searcher = new Searcher();
        this.rateService = rateService;

        this.view.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOrder();
            }
        });
    }

    private void searchOrder() {
        String id = view.getSearchId();
        log.debug("Buscando pedido: {}", id);

        final Order order = searcher.searchOrderById(id, orders);
        if (order == null) {
            log.info("Pedido no encontrado: {}", id);
            view.displayMessage("Order not found.");
            return;
        }

        log.info("Pedido encontrado: {}", id);
        view.displayMessage("Consultando el tipo de cambio...");

        // La consulta de red se hace fuera del hilo de la ventana para no bloquearla
        new SwingWorker<Double, Void>() {
            @Override
            protected Double doInBackground() throws Exception {
                return rateService.getEurUsdRate();
            }

            @Override
            protected void done() {
                double rate = 0;
                try {
                    rate = get();
                    log.info("Tipo de cambio EUR/USD: {}", rate);
                } catch (Exception e) {
                    log.warn("No se pudo obtener el tipo de cambio EUR/USD: {}", e.toString());
                }
                view.displayOrder(order, rate);
            }
        }.execute();
    }
}
