package uk.co.codecritical.asrsdemo.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class ToteTest {

    @Test
    public void test_Constructor1() {

        int id = 1;
        String name = "Eggs";
        int amount = 10;

        Product p = new Product(id, name);

        Tote pa = new Tote(p, amount);

        assertEquals(pa.getAmount(), amount);

    }

    @Test
    public void test_Constructor2() {

        int id = 1;
        String name = "Eggs";
        int amount = 10;

        Tote pa = new Tote(id, name, amount);

        assertEquals(pa.getAmount(), amount);

    }

}