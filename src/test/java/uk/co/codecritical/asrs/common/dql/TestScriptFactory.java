package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableList;
import uk.co.codecritical.asrs.common.dql.executor.DqlExecutor;
import uk.co.codecritical.asrs.common.dql.executor.DqlQuery;
import uk.co.codecritical.asrs.common.dql.interfaces.ScriptFactoryDql;
import uk.co.codecritical.asrs.common.dql.interfaces.StationDql;
import uk.co.codecritical.asrs.common.dql.interfaces.ToteDql;
import uk.co.codecritical.asrs.common.dql.parser.Assignment;

import java.util.Optional;
import java.util.function.Predicate;

public class TestScriptFactory implements ScriptFactoryDql {
    private static final ImmutableList<ToteDql> TOTES = ImmutableList.of(
        new TestTote(1, "EMPTY"),
        new TestTote(2, "EMPTY"),
        new TestTote(3),
        new TestTote(4, "BLUE", "EMPTY")
    );
    private static final ImmutableList<StationDql> STATIONS = ImmutableList.of(
        new TestStation(1, "DECANT"),
        new TestStation(2, "DECANT"),
        new TestStation(3, "PICKING"),
        new TestStation(4, "PICKING")
    );

    public Optional<ToteDql> selectedTote = Optional.empty();
    public Optional<StationDql> selectedStation = Optional.empty();
    public ImmutableList<Assignment> assignments;

    @Override
    public DqlQuery toteRetrievalDql(
            DqlQuery query,
            Predicate<ToteDql> toteDqlPredicate,
            Predicate<StationDql> stationDqlPredicate,
            ImmutableList<Assignment> assignments) {
        this.selectedTote = TOTES.stream()
                .filter(toteDqlPredicate)
                .findFirst();
        this.selectedStation = STATIONS.stream()
                .filter(stationDqlPredicate)
                .findFirst();
        this.assignments = assignments;
        return query;
    }

    @Override
    public DqlQuery toteStorageDql(
            DqlQuery query,
            Predicate<ToteDql> toteDqlPredicate,
            ImmutableList<Assignment> assignments) {
        this.selectedTote = TOTES.stream()
                .filter(toteDqlPredicate)
                .findFirst();
        this.assignments = assignments;
        this.selectedStation = Optional.empty();
        return query;
    }
}
