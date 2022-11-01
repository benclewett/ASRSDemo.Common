package uk.co.codecritical.asrsdemo.common;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final List<Tote> items = new ArrayList<>();

    public Order() {

    }

    public void add(Tote pa) {
        items.add(pa);
    }

    public int length() {
        return items.size();
    }

    public int amount() {
        int l = 0;
        for(Tote pa : items)
            l += pa.getAmount();
        return l;
    }

    @Override
    public String toString() {
        return "Order{" +
                "items=" + items +
                '}';
    }

    public List<Tote> getItems() {
        return items;
    }



}
