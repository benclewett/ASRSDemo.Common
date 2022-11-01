package uk.co.codecritical.asrsdemo.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class ToteTest {

    @Test
    public void test_Constructor() {

        int id = 1;
        String name = "Eggs";
        int amount = 10;
        int barcode = 13;

        Product p = new Product(id, name);

        Tote pa = new Tote(p, amount, barcode);

        assertEquals(pa.getAmount(), amount);
        assertEquals(pa.getBarcode(), barcode);

    }

    @Test
    public void test_Constructor2() {

        int id = 1;
        String name = "Eggs";
        int amount = 10;
        int barcode = 13;

        Tote pa = new Tote(id, name, amount, barcode);

        assertEquals(pa.getAmount(), amount);
        assertEquals(pa.getBarcode(), barcode);

    }

}