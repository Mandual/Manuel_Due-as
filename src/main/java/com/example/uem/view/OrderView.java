package com.example.uem.view;

import javax.swing.*;
import com.example.uem.model.Article;
import com.example.uem.model.Order;
import java.awt.*;
import java.net.URL;

public class OrderView extends JFrame {
    private JTextField searchField = new JTextField(10);
    private JButton searchButton = new JButton("Search");
    private JTextArea resultArea = new JTextArea(12, 50);

    public OrderView() {
        setTitle("Order Management");

        URL iconUrl = OrderView.class.getResource("/app.png");
        if (iconUrl != null) {
            setIconImage(new ImageIcon(iconUrl).getImage());
        }

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

    // rate son los dólares que vale 1 euro; si es 0 o negativo no se muestran los dólares
    public void displayOrder(Order order, double rate) {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido: ").append(order.getId()).append(" (importes en EUR)\n");
        sb.append("--------------------------------------------------\n");
        for (Article art : order.getArticulos()) {
            sb.append(art.getNombre());
            sb.append(" (x").append(art.getCantidad());
            sb.append(" @ ").append(money(art.getPrecio())).append(")\n");
            sb.append("    bruto: ").append(money(art.getBruto()));
            sb.append(" | con descuento: ").append(money(art.getDescuento())).append("\n");
        }
        sb.append("--------------------------------------------------\n");
        sb.append("Total bruto: ").append(totals(order.getBruto(), rate)).append("\n");
        sb.append("Total con descuento: ").append(totals(order.getDescuento(), rate)).append("\n");
        if (rate > 0) {
            sb.append("Tipo de cambio EUR/USD: ").append(String.format("%.4f", rate)).append("\n");
        } else {
            sb.append("USD no disponible (sin tipo de cambio)\n");
        }
        resultArea.setText(sb.toString());
    }

    public void displayMessage(String message) {
        resultArea.setText(message);
    }

    private String totals(double eur, double rate) {
        String text = money(eur) + " EUR";
        if (rate > 0) {
            text = text + " / " + money(eur * rate) + " USD";
        }
        return text;
    }

    private String money(double value) {
        return String.format("%.2f", value);
    }
}
