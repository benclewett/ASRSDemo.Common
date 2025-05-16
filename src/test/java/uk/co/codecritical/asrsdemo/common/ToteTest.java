package uk.co.codecritical.asrsdemo.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class ToteTest {
    private static final String EGGS = "Eggs";
    private static final int AMOUNT = 10;

    @Test
    public void test_Constructor() {
        int id = 1;

        Sku sku = new Sku(id, EGGS);

        Tote tote = Tote.builder(id).setSku(sku).setAmount(AMOUNT).build();

        assertEquals(id, tote.id);
        assertEquals(sku, tote.sku);
        assertEquals(AMOUNT, tote.amount);
    }
}