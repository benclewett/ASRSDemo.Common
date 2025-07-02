package uk.co.codecritical.asrs.common;

import org.junit.jupiter.api.Test;
import uk.co.codecritical.asrs.common.entity.Sku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SkuTest {

    private static final String EGGS = "EGGS";

    @Test
    public void testConstructor() {
        int id = 1;

        Sku p = new Sku(id, EGGS);

        assertEquals(id, p.id);
        assertEquals(EGGS, p.name);
    }

    @Test
    public void testEquals() {
        int id0 = 1;
        int id1 = 2;

        Sku p0 = new Sku(id0, EGGS);
        Sku p1 = new Sku(id1, EGGS);

        assertEquals(id0, p0.id);
        assertEquals(EGGS, p0.name);

        assertNotEquals(p0, p1);

        Sku p2 = new Sku(id0, EGGS);

        assertEquals(p2, p0);
        assertNotEquals(p2, p1);
    }
}