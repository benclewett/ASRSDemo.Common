package uk.co.codecritical.asrs.common;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class Sku {
    public final int id;
    public final String name;

    public Sku(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sku sku = (Sku) o;
        return id == sku.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
