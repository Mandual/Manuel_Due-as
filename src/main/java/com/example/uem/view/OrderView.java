package com.example.uem.view;

import javax.swing.*;
import com.example.uem.model.Article;
import com.example.uem.model.Order;
import java.awt.*;

public class OrderView extends JFrame {
    private JTextField searchField = new JTextField(10);
    private JButton searchButton = new JButton("Search");
    private JTextArea resultArea = new JTextArea(10, 40);

    public OrderView() {
        setTitle("Order Management");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("Order ID:"));
        add(searchField);
        add(searchButton);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea));

        pack();
        setVisible(true);
    }

    public String getSearchId() {
        return searchField.getText().trim();
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    // rate multiplica los importes mostrados (1.0 = sin conversión)
    public void displayOrder(Order order, double rate) {
        StringBuilder sb = new StringBuilder();
        sb.append("Order: ").append(order.getId()).append("\n");
        sb.append("----------------------------------------\n");
        for (Article art : order.getArticulos()) {
            sb.append(art.getNombre());
            sb.append(" (x").append(art.getCantidad());
            sb.append(" @ ").append(money(art.getPrecio() * rate)).append(")\n");
            sb.append("    gross: ").append(money(art.getBruto() * rate));
            sb.append(" | discounted: ").append(money(art.getDescuento() * rate)).append("\n");
        }
        sb.append("----------------------------------------\n");
        sb.append("Gross total: ").append(money(order.getBruto() * rate)).append("\n");
        sb.append("Total with discount: ").append(money(order.getDescuento() * rate)).append("\n");
        resultArea.setText(sb.toString());
    }

    public void displayMessage(String message) {
        resultArea.setText(message);
    }

    private String money(double value) {
        return String.format("%.2f", value);
    }
}
