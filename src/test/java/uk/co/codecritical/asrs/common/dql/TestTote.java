package uk.co.codecritical.asrs.common.dql;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import uk.co.codecritical.asrs.common.dql.interfaces.ToteDql;
import uk.co.codecritical.asrs.common.dql.parser.ToteTag;

public class TestTote implements ToteDql {
    final int id;
    ImmutableSet<String> properties;
    ImmutableSet<ToteTag> tags;

    public TestTote(int id, String... properties) {
        this.id = id;
        this.properties = ImmutableSet.copyOf(properties);
        this.tags = ImmutableSet.of();
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
    public ImmutableSet<ToteTag> getTags() {
        return tags;
    }

    @Override
    public int getAmount() {
        return 0;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("properties", properties)
                .toString();
    }
}
