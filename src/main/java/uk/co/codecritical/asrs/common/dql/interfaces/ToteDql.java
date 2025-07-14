package uk.co.codecritical.asrs.common.dql.interfaces;

import com.google.common.collect.ImmutableSet;
import uk.co.codecritical.asrs.common.dql.parser.ToteTag;

public interface ToteDql {
    String filter(String value);
    int getId();
    ImmutableSet<String> getProperties();
    ImmutableSet<ToteTag> getTags();
    int getAmount();
}
