package uk.co.codecritical.asrs.common;

import com.google.common.base.MoreObjects;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

public class Tote {
    public final int id;
    public final Optional<Sku> sku;
    public final int amount;
    public final Optional<Pos> gridPos;

    private Tote(int id, Optional<Sku> sku, int amount, Optional<Pos> gridPos) {
        this.id = id;
        this.sku = sku;
        this.amount = amount;
        this.gridPos = gridPos;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("sku", sku)
                .add("amount", amount)
                .add("gridPos", gridPos)
                .toString();
    }

    public String niceProduct() {
        return String.format("%03d,%s,%d",
                id,
                sku.map(s -> s.name).orElse(""),
                amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tote tote = (Tote) o;
        return amount == tote.amount && this.sku.equals(tote.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, sku);
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
                .setGridPos(gridPos)
                .setSku(sku);
    }

    public static class Builder {
        private final int id;
        private Optional<Sku> sku = Optional.empty();
        private int amount = 0;
        private Optional<Pos> gridPos = Optional.empty();

        private Builder() {
            this.id = getNextId();
        }

        private Builder(int id) {
            this.id = id;
            nextId = Math.max(this.id, nextId) + 1;
        }

        public Builder setSku(Sku sku) {
            this.sku = Optional.of(sku);
            return this;
        }

        public Builder setSku(Optional<Sku> sku) {
            this.sku = sku;
            return this;
        }

        public Builder setSku(int productId, String productName) {
            this.sku = Optional.of(new Sku(productId, productName));
            return this;
        }

        public Builder setGridPos(Optional<Pos> gridPos) {
            this.gridPos = gridPos;
            return this;
        }

        public Builder removeSku() {
            this.sku = Optional.empty();
            return this;
        }

        public Builder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public Tote build() {
            assert (sku.isEmpty() && amount == 0 || sku.isPresent() && amount != 0);
            return new Tote(id, sku, amount, gridPos);
        }
    }

    //endregion
}
