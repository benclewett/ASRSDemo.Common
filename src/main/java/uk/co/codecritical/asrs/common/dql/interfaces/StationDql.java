package uk.co.codecritical.asrs.common.dql.interfaces;

import java.util.Optional;

public interface StationDql {
    String filter(String value);

    int getId();
    Optional<String> getCapability();

    void setCapability(Optional<String> capability);
}
