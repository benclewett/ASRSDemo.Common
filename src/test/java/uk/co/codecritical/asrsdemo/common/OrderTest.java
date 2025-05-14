package uk.co.codecritical.asrsdemo.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderTest {
    final static Tote PA_1 = Tote.builder().setProduct(1, "EGGS").setAmount(2).build();
    final static Tote PA_2 = Tote.builder().setProduct(2, "MILK").setAmount(3).build();
    final static Tote PA_3 = Tote.builder().setProduct(3, "BEER").setAmount(5).build();

    @Test
    public void testAdd_Length() {
        Route o = Route.builder()
                .addTote(PA_1)
                .addTote(PA_2)
                .addTote(PA_3)
                .build();

        assertEquals(3, o.length());
    }

    @Test
    public void testAdd_Amount() {
        Route o = Route.builder()
                .addTote(PA_1)
                .addTote(PA_2)
                .addTote(PA_3)
                .build();

        assertEquals(10, o.amount());
    }
}