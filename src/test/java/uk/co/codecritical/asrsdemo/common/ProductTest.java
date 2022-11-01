package uk.co.codecritical.asrsdemo.common;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Test;
public class ProductTest {

    @Test
    public void testConstructor() {

        int id = 1;
        String name = "Eggs";

        Product p = new Product(id, name);

        assertEquals(p.getId(), id);
        assertEquals(p.getName(), name);
    }

    @Test
    public void testEquals() {

        int id0 = 1;
        int id1 = 2;
        String name = "EGGS";

        Product p0 = new Product(id0, name);
        Product p1 = new Product(id1, name);

        assertEquals(p0.getId(), id0);
        assertEquals(p0.getName(), name);

        assertNotEquals(p0, p1);

        Product p2 = new Product(id0, name);

        assertEquals(p2, p0);
        assertNotEquals(p2, p1);

    }

}