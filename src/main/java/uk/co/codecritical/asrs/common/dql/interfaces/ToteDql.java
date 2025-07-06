package uk.co.codecritical.asrs.common.dql.interfaces;

import com.google.common.collect.ImmutableSet;
import uk.co.codecritical.asrs.common.dql.parser.ToteMeta;

public interface ToteDql {
    String filter(String value);
    int getId();
    ImmutableSet<String> getProperties();
    ImmutableSet<ToteMeta> getMeta();
    int getAmount();
}
