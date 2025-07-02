package uk.co.codecritical.asrs.common.dql;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import uk.co.codecritical.asrs.common.dql.interfaces.ToteDql;

public class TestTote implements ToteDql {
    final int id;
    ImmutableSet<String> properties;
    ImmutableSet<String> meta;

    public TestTote(int id, String... properties) {
        this.id = id;
        this.properties = ImmutableSet.copyOf(properties);
        this.meta = ImmutableSet.of();
    }

    @Override
    public String filter(String value) {
        return value.toUpperCase().strip();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public ImmutableSet<String> getProperties() {
        return properties;
    }

    @Override
    public ImmutableSet<String> getMeta() {
        return meta;
    }

    @Override
    public int getAmount() {
        return 0;
    }

    @Override
    public ToteDql setProperties(ImmutableSet<String> properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public ToteDql setMeta(ImmutableSet<String> meta) {
        this.meta = meta;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("properties", properties)
                .toString();
    }
}
