package uk.co.codecritical.asrs.common.dql.entity;

import com.google.common.collect.ImmutableList;
import uk.co.codecritical.asrs.common.dql.parser.Assignment;

import java.util.function.Predicate;

public interface DqlGrid {
    void toteRetrievalDql(
            Predicate<ToteDql> toteDqlPredicate,
            Predicate<StationDql> stationDqlPredicate,
            ImmutableList<Assignment> assignments
            );
}
