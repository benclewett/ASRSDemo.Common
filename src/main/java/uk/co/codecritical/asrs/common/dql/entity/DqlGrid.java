package uk.co.codecritical.asrs.common.dql.entity;

import java.util.function.Predicate;

public interface DqlGrid {
    void binToStation(Predicate<ToteDql> toteDqlPredicate, Predicate<StationDql> stationDqlPredicate);
}
