package uk.co.codecritical.asrsdemo.common;

import java.util.Objects;

public class Pos {

    private int x;
    private int y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pos(Pos pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public int incX(int inc) {
        x += inc;
        return x;
    }

    public int incY(int inc) {
        y += inc;
        return y;
    }

    public Pos inc(Pos inc) {
        x += inc.x;
        y += inc.y;
        return this;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPos(Pos pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public Pos add(Pos pos) {
        this.x += pos.x;
        this.y += pos.y;
        return this;
    }
    public Pos subtract(Pos pos) {
        this.x -= pos.x;
        this.y -= pos.y;
        return this;
    }

    public static Pos add(Pos p0, Pos p1) {
        return new Pos(p0.x + p1.x, p0.y + p1.y);
    }

    public static Pos subtract(Pos p0, Pos p1) {
        return new Pos(p0.x - p1.x, p0.y - p1.y);
    }

    @Override
    public String toString() {
        return "Pos(" +x + "," + y + ")";
    }

    public String niceCoordinate() {
        return String.format("(%d,%d)", x, y);
    }

}
