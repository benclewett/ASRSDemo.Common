package uk.co.codecritical.asrs.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import uk.co.codecritical.asrs.common.dql.interfaces.ToteDql;

import java.util.Objects;
import java.util.Optional;

public class Tote implements ToteDql {
    public final int id;
    @JsonIgnore
    public final Optional<Sku> sku;
    @JsonIgnore
    public final int amount;
    @JsonIgnore
    public final Optional<Pos> gridPos;
    public final TokenSet properties;
    public final Availability available;

    private Tote(int id, Optional<Sku> sku, int amount, Optional<Pos> gridPos, TokenSet properties, Availability available) {
        this.id = id;
        this.sku = sku;
        this.amount = amount;
        this.gridPos = gridPos;
        this.properties = properties;
        this.available = available;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("sku", sku)
                .add("amount", amount)
                .add("gridPos", gridPos)
                .add("properties", properties)
                .add("available", available)
                .toString();
    }

    public enum Availability {
        AVAILABLE_RETRIEVE,
        AVAILABLE_STORE,
        AVAILABLE_PICKING,
        UNAVAILABLE
    }

    public String niceProduct() {
        return sku
                .map(value -> String.format("%d,%s,%d", id, value.name, amount))
                .orElseGet(() -> String.format("%d,(empty)", id));
    }

    @Override
    public String filter(String value) {
        return TokenSet.filter(value);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public ImmutableSet<String> getProperties() {
        return properties.get();
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public ToteDql setProperties(ImmutableSet<String> properties) {
        return this.mutate().setProperties(properties).build();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tote tote = (Tote) o;
        return id == tote.id
                && amount == tote.amount
                && Objects.equals(sku, tote.sku)
                && Objects.equals(gridPos, tote.gridPos)
                && Objects.equals(properties, tote.properties)
                && Objects.equals(available, tote.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, amount, gridPos, properties, available);
    }

    //region Builder

    private static int nextId = 1;
    private synchronized static int getNextId() {
        return nextId++;
    }

    public static Tote createEmptyTote() {
        return new Builder().build();
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
                .setSku(sku)
                .setProperties(properties)
                .setAvailable(available);
    }

    public static class Builder {
        private final int id;
        private Optional<Sku> sku = Optional.empty();
        private int amount = 0;
        private Optional<Pos> gridPos = Optional.empty();
        private TokenSet properties = TokenSet.EMPTY;
        private Availability available = Availability.AVAILABLE_RETRIEVE;
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
        public Builder setGridPos(Pos gridPos) {
            this.gridPos = Optional.of(gridPos);
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
        public Builder clearProperties() {
            this.properties = TokenSet.EMPTY;
            return this;
        }
        public Builder setProperties(ImmutableSet<String> properties) {
            this.properties = TokenSet.of(properties);
            return this;
        }
        public Builder setProperties(TokenSet properties) {
            this.properties = properties;
            return this;
        }
        public Builder addProperty(String property) {
            this.properties = this.properties.mutate().addToken(TokenSet.filter(property)).build();
            return this;
        }
        public Builder delProperty(String property) {
            this.properties = this.properties.mutate().removeToken(TokenSet.filter(property)).build();
            return this;
        }
        public Builder setAvailable(Availability available) {
            this.available = available;
            return this;
        }
        public Tote build() {
            assert (sku.isEmpty() && amount == 0 || sku.isPresent() && amount != 0);
            return new Tote(id, sku, amount, gridPos, properties, available);
        }
    }

    //endregion
}
