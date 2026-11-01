package com.example.uem.model;

import java.util.ArrayList;
import java.util.List;

public class Searcher {
    public boolean searchExactPhrase(String phrase, List<String> list) {
        for (String item : list) {
            if (item.equals(phrase)) {
                return true;
            }
        }
        return false;
    }

    public boolean searchWord(String word, List<String> list) {
        return list.contains(word);
    }

    public String getWordByIndex(List<String> list, int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null;
    }

    public List<String> searchByPrefix(String prefix, List<String> list) {
        List<String> results = new ArrayList<>();
        for (String element : list) {
            if (element.startsWith(prefix)) {
                results.add(element);
            }
        }
        return results;
    }

    public List<String> filterByKeyword(String keyword, List<String> list) {
        List<String> results = new ArrayList<>();
        for (String element : list) {
            if (element.contains(keyword)) {
                results.add(element);
            }
        }
        return results;
    }

    // Busca un pedido por su id en la lista de pedidos; devuelve null si no existe
    public Order searchOrderById(String id, List<Order> orders) {
        if (id == null) {
            return null;
        }
        for (Order order : orders) {
            if (id.equals(order.getId())) {
                return order;
            }
        }
        return null;
    }
}
