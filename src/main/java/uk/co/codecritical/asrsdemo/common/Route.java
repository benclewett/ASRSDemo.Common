package uk.co.codecritical.asrsdemo.common;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

public class Route {

    public final ImmutableSet<Tote> totes;

    public Route(ImmutableSet<Tote> totes) {
        this.totes = totes;
    }

    public int length() {
        return totes.size();
    }

    public int amount() {
        return totes.stream().mapToInt(t -> t.amount).sum();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("totes", totes)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        ImmutableSet.Builder<Tote> totes = ImmutableSet.builder();

        private Builder() {
        }

        public Builder addTote(Tote tote) {
            totes.add(tote);
            return this;
        }

        public Route build() {
            return new Route(totes.build());
        }
    }
}
