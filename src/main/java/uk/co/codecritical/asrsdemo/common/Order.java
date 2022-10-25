package uk.co.codecritical.asrsdemo.common;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final List<ProductAmount> items = new ArrayList<>();

    public Order() {

    }

    public void add(ProductAmount pa) {
        items.add(pa);
    }

    public int length() {
        return items.size();
    }

    public int amount() {
        int l = 0;
        for(ProductAmount pa : items)
            l += pa.getAmount();
        return l;
    }

    @Override
    public String toString() {
        return "Order{" +
                "items=" + items +
                '}';
    }

    public List<ProductAmount> getItems() {
        return items;
    }



}
