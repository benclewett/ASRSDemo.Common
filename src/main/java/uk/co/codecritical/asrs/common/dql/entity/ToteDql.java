package uk.co.codecritical.asrs.common.dql.entity;

import com.google.common.collect.ImmutableSet;

public interface ToteDql {
    String filter(String value);

    int getId();
    ImmutableSet<String> getProperties();
    int getAmount();

    void setProperties(ImmutableSet<String> properties);
}
