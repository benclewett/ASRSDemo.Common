package uk.co.codecritical.asrs.common.entity;

import java.util.Objects;

public class Pos {
    public static final Pos ZERO = new Pos(0,0);
    public final int x;
    public final int y;

    public static Pos of(int x, int y) {
        return new Pos(x, y);
    }

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pos(Pos pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public Pos incX(int inc) {
        return new Pos(x + inc, y);
    }

    public Pos incY(int inc) {
        return new Pos(x, y + inc);
    }

    public Pos inc(Pos inc) {
        return new Pos(x + inc.x, y + inc.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos pos = (Pos) o;
        return x == pos.x && y == pos.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Pos add(Pos pos) {
        return new Pos(x + pos.x, y + pos.y);
    }

    public Pos subtract(Pos pos) {
        return new Pos(x - pos.x, y - pos.y);
    }

    public static Pos add(Pos p0, Pos p1) {
        return new Pos(p0.x + p1.x, p0.y + p1.y);
    }

    public static Pos subtract(Pos p0, Pos p1) {
        return new Pos(p0.x - p1.x, p0.y - p1.y);
    }

    @Override
    public String toString() {
        if (ZERO.equals(this)) {
            return "Pos{ZERO}";
        } else {
            return "Pos{" + x + "," + y + "}";
        }
    }

    public String niceCoordinate() {
        return String.format("(%d,%d)", x, y);
    }
}
