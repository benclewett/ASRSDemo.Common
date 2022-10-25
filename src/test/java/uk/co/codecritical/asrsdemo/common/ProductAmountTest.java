package uk.co.codecritical.asrsdemo.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProductAmountTest {

    @Test
    public void test_Constructior() {

        int id = 1;
        String name = "Eggs";
        int amount = 10;

        Product p = new Product(id, name);

        ProductAmount pa = new ProductAmount(p, amount);

        assertEquals(pa.getAmount(), amount);

    }

}