package uk.co.codecritical.asrsdemo.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class OrderTest {

    static Tote pa1 = new Tote(1, "EGGS", 2);
    static Tote pa2 = new Tote(2, "MILK", 3);
    static Tote pa3 = new Tote(3, "BEER", 5);

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