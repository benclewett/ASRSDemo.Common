package uk.co.codecritical.asrs.common;

import org.junit.jupiter.api.Test;
import uk.co.codecritical.asrs.common.entity.Sku;
import uk.co.codecritical.asrs.common.entity.Tote;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToteTest {
    private static final String EGGS = "Eggs";
    private static final int AMOUNT = 10;

    @Test
    public void test_Constructor() {
        int id = 1;

        Sku sku = new Sku(id, EGGS);

        Tote tote = Tote.builder(id).setSku(sku).setAmount(AMOUNT).build();

        assertEquals(id, tote.id);
        assertEquals(sku, tote.sku.get());
        assertEquals(AMOUNT, tote.amount);
    }
}