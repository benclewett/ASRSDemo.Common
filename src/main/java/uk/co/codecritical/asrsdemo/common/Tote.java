package uk.co.codecritical.asrsdemo.common;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class Tote {
    public final int id;
    public final Sku sku;
    public final int amount;

    private Tote(int id, Sku product, int amount) {
        this.id = id;
        this.sku = product;
        this.amount = amount;
    }

    //region toString

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("sku", sku)
                .add("amount", amount)
                .toString();
    }

    public String niceProduct() {
        return String.format("%03d,%s,%d",
                id,
                sku.name,
                amount);
    }

    public static String niceProduct(Tote pa) {
        if (pa == null)
            return "";
        else
            return pa.niceProduct();
    }

    //endregion

    //region Comparison

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tote tote = (Tote) o;
        return id == tote.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    //endregion

    //region Builder

    private static int nextId = 1;
    private synchronized static int getNextId() {
        return nextId++;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(int id) {
        return new Builder(id);
    }

    public Builder mutate() {
        return new Builder(this.id)
                .setAmount(amount)
                .setSku(sku);
    }

    public static class Builder {
        private int id;
        private Sku sku = null;
        private int amount;

        private Builder() {
            this.id = getNextId();
        }

        private Builder(int id) {
            this.id = id;
            nextId = Math.max(this.id, nextId) + 1;
        }

        public Builder setSku(Sku sku) {
            this.sku = sku;
            return this;
        }

        public Builder setSku(int productId, String productName) {
            this.sku = new Sku(productId, productName);
            return this;
        }

        public Builder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public Tote build() {
            return new Tote(id, sku, amount);
        }
    }

    //endregion

}
