package uk.co.codecritical.asrsdemo.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class ToteTest {
    private static final String EGGS = "Eggs";
    private static final int AMOUNT = 10;

    @Test
    public void test_Constructor1() {
        int id = 1;

        Sku p = new Sku(id, EGGS);

        Tote pa = Tote.builder().setSku(p).setAmount(AMOUNT).build();

        assertEquals(AMOUNT, pa.amount);
    }
}