package uk.co.codecritical.asrs.common;

import org.junit.jupiter.api.Test;
import uk.co.codecritical.asrs.common.entity.Sku;
import uk.co.codecritical.asrs.common.entity.Stock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StockTest {
    private static final String EGGS = "Eggs";
    private static final String FISH = "Fish";
    private static final int AMOUNT = 10;
    private static final Sku SKU_1 = new Sku(1, EGGS);
    private static final Sku SKU_2 = new Sku(2, FISH);

    @Test
    public void testConstructor() {

        Stock stock = Stock.builder()
                .setSku(SKU_1)
                .setAmount(AMOUNT)
                .build();

        assertEquals(SKU_1, stock.sku);
        assertEquals(AMOUNT, stock.amount);
    }

    @Test
    void testEquals() {
        Stock stock1 = Stock.builder().setSku(SKU_1).setAmount(AMOUNT).build();
        Stock stock2 = Stock.builder().setSku(SKU_1).setAmount(AMOUNT).build();
        Stock stock3 = Stock.builder().setSku(SKU_2).setAmount(AMOUNT).build();
        Stock stock4 = Stock.builder().setSku(SKU_1).setAmount(AMOUNT+1).build();

        assertEquals(stock1, stock2);
        assertNotEquals(stock1, stock3);
        assertNotEquals(stock1, stock4);
    }
}
