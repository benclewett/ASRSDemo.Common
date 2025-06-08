package uk.co.codecritical.asrs.common.dql.interfaces;

import com.google.common.collect.ImmutableList;
import uk.co.codecritical.asrs.common.dql.executor.DqlQuery;
import uk.co.codecritical.asrs.common.dql.parser.Assignment;

import java.util.function.Predicate;

public interface DqlExecutorListener {

    DqlQuery toteRetrievalDql(
            DqlQuery query,
            Predicate<ToteDql> toteDqlPredicate,
            Predicate<StationDql> stationDqlPredicate,
            ImmutableList<Assignment> assignments
            );

    DqlQuery toteStorageDql(
            DqlQuery query,
            Predicate<ToteDql> toteDqlPredicate,
            ImmutableList<Assignment> assignments
    );

    DqlQuery toteReleaseDql (
            DqlQuery query,
            Predicate<ToteDql> toteDqlPredicate,
            ImmutableList<Assignment> assignments
    );
}
