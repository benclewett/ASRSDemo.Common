package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableSet;
import uk.co.codecritical.asrs.common.dql.entity.ToteDql;

public class TestTote implements ToteDql {
    final int id;
    ImmutableSet<String> properties;

    public TestTote(int id, String... properties) {
        this.id = id;
        this.properties = ImmutableSet.copyOf(properties);
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
    public void setProperties(ImmutableSet<String> properties) {
        this.properties = properties;
    }
}
