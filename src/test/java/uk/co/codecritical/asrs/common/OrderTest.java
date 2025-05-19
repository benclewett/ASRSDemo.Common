package uk.co.codecritical.asrs.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {
    final static Tote PA_1 = Tote.builder().setSku(1, "EGGS").setAmount(2).build();
    final static Tote PA_2 = Tote.builder().setSku(2, "MILK").setAmount(3).build();
    final static Tote PA_3 = Tote.builder().setSku(3, "BEER").setAmount(5).build();

    @Test
    public void testAdd_Length() {
        Route o = Route.builder()
                .setTote(PA_1)
                .setTote(PA_2)
                .setTote(PA_3)
                .build();

        assertEquals(3, o.size());
    }

    @Test
    public void testAdd_Amount() {
        Route o = Route.builder()
                .setTote(PA_1)
                .setTote(PA_2)
                .setTote(PA_3)
                .build();

        assertEquals(10, o.amount());
    }
}