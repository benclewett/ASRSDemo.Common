package uk.co.codecritical.asrs.common.dql.interfaces;

import com.google.common.collect.ImmutableSet;

public interface ToteDql {
    String filter(String value);

    int getId();
    ImmutableSet<String> getProperties();
    int getAmount();

    ToteDql setProperties(ImmutableSet<String> properties);
}
