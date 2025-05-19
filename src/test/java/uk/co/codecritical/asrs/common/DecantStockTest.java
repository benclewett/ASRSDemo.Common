package uk.co.codecritical.asrs.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DecantStockTest {
    private static final String EGGS = "Eggs";
    private static final String FISH = "Fish";
    private static final int AMOUNT = 10;
    private static final Sku SKU_1 = new Sku(1, EGGS);
    private static final Sku SKU_2 = new Sku(2, FISH);

    @Test
    public void test_Constructor() {
        int id = 1;

        DecantStock decantStock = DecantStock.builder(id).setSku(SKU_1).setAmount(AMOUNT).build();

        Assertions.assertEquals(id, decantStock.id);
        Assertions.assertEquals(SKU_1, decantStock.sku);
        Assertions.assertEquals(AMOUNT, decantStock.amount);
    }

    @Test
    void testEquals() {
        DecantStock decantStock1 = DecantStock.builder().setSku(SKU_1).setAmount(AMOUNT).build();
        DecantStock decantStock2 = DecantStock.builder().setSku(SKU_1).setAmount(AMOUNT).build();
        DecantStock decantStock3 = DecantStock.builder().setSku(SKU_2).setAmount(AMOUNT).build();
        DecantStock decantStock4 = DecantStock.builder().setSku(SKU_1).setAmount(AMOUNT+1).build();

        Assertions.assertEquals(decantStock1, decantStock2);
        Assertions.assertNotEquals(decantStock1, decantStock3);
        Assertions.assertNotEquals(decantStock1, decantStock4);
    }
}
