package uk.co.codecritical.asrsdemo.common;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class DecantStock {
    public final int id;
    public final Sku sku;
    public final int amount;

    private DecantStock(int id, Sku sku, int amount) {
        this.id = id;
        this.sku = sku;
        this.amount = amount;
    }

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("sku", sku)
                .add("amount", amount)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DecantStock that = (DecantStock) o;
        return amount == that.amount && Objects.equals(sku, that.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, amount);
    }

    public static class Builder {
        private final int id;
        private Sku sku = null;
        private int amount = 0;

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

        public DecantStock build() {
            assert (sku != null);
            assert (amount != 0);
            return new DecantStock(id, sku, amount);
        }
    }

    //endregion
}
