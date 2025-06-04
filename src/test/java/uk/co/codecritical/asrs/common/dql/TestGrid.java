package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableList;
import uk.co.codecritical.asrs.common.dql.entity.StationDql;
import uk.co.codecritical.asrs.common.dql.entity.ToteDql;
import uk.co.codecritical.asrs.common.dql.entity.DqlGrid;

import java.util.Optional;
import java.util.function.Predicate;

public class TestGrid implements DqlGrid {
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

    @Override
    public void binToStation(Predicate<ToteDql> toteDqlPredicate, Predicate<StationDql> stationDqlPredicate) {
        selectedTote = TOTES.stream()
                .filter(toteDqlPredicate)
                .findFirst();
        selectedStation = STATIONS.stream()
                .filter(stationDqlPredicate)
                .findFirst();
    }
}
