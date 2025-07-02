package uk.co.codecritical.asrs.common.entity;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/* A quantity of sku */
public class Stock {
    public final Sku sku;
    public final int amount;

    private Stock(Sku sku, int amount) {
        this.sku = sku;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sku", sku)
                .add("amount", amount)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Stock that = (Stock) o;
        return amount == that.amount && Objects.equals(sku, that.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, amount);
    }

    //region Builder

    private static int nextId = 1;
    private synchronized static int getNextId() {
        return nextId++;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder mutate() {
        return new Builder()
                .setAmount(amount)
                .setSku(sku);
    }

    public static class Builder {
        private Sku sku = null;
        private int amount = 0;

        private Builder() {
        }

        public Builder setSku(Sku sku) {
            this.sku = sku;
            return this;
        }

        public Builder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public Stock build() {
            assert (sku != null);
            assert (amount != 0);
            return new Stock(sku, amount);
        }
    }

    //endregion
}
