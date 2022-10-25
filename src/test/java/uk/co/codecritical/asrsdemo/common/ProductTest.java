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


}