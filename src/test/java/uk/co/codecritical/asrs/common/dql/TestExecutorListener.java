package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import uk.co.codecritical.asrs.common.dql.executor.DqlQuery;
import uk.co.codecritical.asrs.common.dql.interfaces.DqlExecutorListener;
import uk.co.codecritical.asrs.common.dql.interfaces.StationDql;
import uk.co.codecritical.asrs.common.dql.interfaces.ToteDql;
import uk.co.codecritical.asrs.common.dql.parser.Assignment;

import java.util.Optional;
import java.util.function.Predicate;

public class TestExecutorListener implements DqlExecutorListener {
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
    public Optional<ToteDql> selectedToteInTo = Optional.empty();
    public Optional<ToteDql> selectedToteOutOf = Optional.empty();
    public Optional<StationDql> selectedStation = Optional.empty();
    public ImmutableList<Assignment> assignments;
    public int sku;
    public int amount;

    @BeforeEach
    void beforeEach() {
        sku = Integer.MAX_VALUE;
        amount = Integer.MAX_VALUE;
    }

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

    @Override
    public DqlQuery toteReleaseDql(
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

    @Override
    public DqlQuery pickIntoDql(DqlQuery query, Predicate<ToteDql> totePickInto, ImmutableList<Assignment> assignments) {
        this.amount = getAmount(assignments);
        this.sku = getSku(assignments);
        this.assignments = assignments;
        this.selectedToteInTo = TOTES.stream()
                .filter(totePickInto)
                .findFirst();
        return query;
    }

    @Override
    public DqlQuery pickOutOfDql(DqlQuery query, Predicate<ToteDql> totePickInto, ImmutableList<Assignment> assignments) {
        this.amount = getAmount(assignments);
        this.sku = getSku(assignments);
        this.assignments = assignments;
        this.selectedToteOutOf = TOTES.stream()
                .filter(totePickInto)
                .findFirst();
        return query;
    }

    @Override
    public DqlQuery pickToteToToteDql(
            DqlQuery query,
            Predicate<ToteDql> totePickOutOf,
            Predicate<ToteDql> totePickInto,
            ImmutableList<Assignment> assignments) {
        this.amount = getAmount(assignments);
        this.sku = getSku(assignments);
        this.assignments = assignments;
        this.selectedToteInTo = TOTES.stream()
                .filter(totePickInto)
                .findFirst();
        this.selectedToteOutOf = TOTES.stream()
                .filter(totePickOutOf)
                .findFirst();
        return query;
    }

    //--------------------------------- Supporting Code ------------------------------------

    private int getAmount(ImmutableList<Assignment> assignments) {
        return Integer.parseInt(assignments.stream()
                .filter(a -> "AMOUNT".equals(a.name()))
                .map(Assignment::value)
                .findFirst()
                .orElseThrow());
    }

    private int getSku(ImmutableList<Assignment> assignments) {
        return Integer.parseInt(assignments.stream()
                .filter(a -> "SKU".equals(a.name()))
                .map(Assignment::value)
                .findFirst()
                .orElseThrow());
    }
}
