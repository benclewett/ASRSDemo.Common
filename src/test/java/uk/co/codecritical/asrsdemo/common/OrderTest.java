package uk.co.codecritical.asrsdemo.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class OrderTest {

    static ProductAmount pa1 = new ProductAmount(1, "Eggs", 2);
    static ProductAmount pa2 = new ProductAmount(2, "Milk", 3);
    static ProductAmount pa3 = new ProductAmount(3, "Bread", 5);

    @Test
    public void testAdd_Length() {

        Order o = new Order();
        o.add(pa1);
        o.add(pa2);
        o.add(pa3);

        assertEquals(3, o.length());
    }

    @Test
    public void testAdd_Amount() {

        Order o = new Order();
        o.add(pa1);
        o.add(pa2);
        o.add(pa3);

        assertEquals(10, o.amount());
    }

}