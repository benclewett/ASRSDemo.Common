package uk.co.codecritical.asrs.common;

import org.junit.jupiter.api.Test;
import uk.co.codecritical.asrs.common.entity.Pos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class PosTest  {

    static final int X = 3;
    static final int Y = 5;

    @Test
    public void test_constructor() {
        var pos = new Pos(X, Y);

        assertEquals(X, pos.x);
        assertEquals(Y, pos.y);

        var pos2 = new Pos(pos);

        assertEquals(X, pos2.x);
        assertEquals(Y, pos2.y);
    }

    @Test
    public void test_inc() {

        var pos = new Pos(7,11);

        pos = pos.incX(1);
        assertEquals(new Pos(8, 11), pos);

        pos = pos.incY(3);
        assertEquals(new Pos(8, 14), pos);

        pos = pos.incX(-5);
        assertEquals(new Pos(3, 14), pos);

        pos = pos.incY(-15);
        assertEquals(new Pos(3, -1), pos);

        pos = pos.inc(new Pos(17, -17));
        assertEquals(new Pos(20, -18), pos);
    }

    @Test
    public void test_Equals() {
        var pos1 = new Pos(19, 17);
        var pos2 = new Pos(19, 17);

        System.out.println("Test the same:");
        System.out.println("  Pos1: " + pos1);
        System.out.println("  Pos2: " + pos2);

        assertNotSame(pos1, pos2);
        assertEquals(pos1, pos2);
    }

    @Test
    public void test_NotEquals() {
        var pos1 = new Pos(19, 17);
        var pos2 = new Pos(19, 18);

        System.out.println("Test not the same.");
        System.out.println("  Pos1: " + pos1);
        System.out.println("  Pos2: " + pos2);

        assertNotSame(pos1, pos2);
        assertNotEquals(pos1, pos2);
    }

    @Test
    public void test_add() {
        Pos p0 = new Pos(2, 5);
        Pos p1 = new Pos(7, 11);

        Pos p2 = p0.add(p1);

        assertEquals(new Pos(9, 16), p2);
    }

    @Test
    public void test_subtract() {
        Pos p0 = new Pos(2, 5);
        Pos p1 = new Pos(7, 11);

        Pos p2 = p0.subtract(p1);

        assertEquals(new Pos(-5, -6), p2);
    }

    @Test
    public void test_add_static() {
        Pos p0 = new Pos(2, 5);
        Pos p1 = new Pos(7, 11);

        Pos p2 = Pos.add(p0, p1);

        assertEquals(new Pos(9, 16), p2);
    }

    @Test
    public void test_subtract_static() {
        Pos p0 = new Pos(2, 5);
        Pos p1 = new Pos(7, 11);

        Pos p2 = Pos.subtract(p0, p1);

        assertEquals(new Pos(-5, -6), p2);
    }

}