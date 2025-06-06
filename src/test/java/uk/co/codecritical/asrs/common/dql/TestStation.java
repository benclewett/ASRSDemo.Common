package uk.co.codecritical.asrs.common.dql;

import com.google.common.base.MoreObjects;
import uk.co.codecritical.asrs.common.dql.interfaces.StationDql;

import java.util.Optional;

public class TestStation implements StationDql {
    final int id;
    Optional<String> capability;

    public TestStation(int id, String capability) {
        this.id = id;
        this.capability = Optional.of(capability);
    }

    public TestStation(int id, Optional<String> capability) {
        this.id = id;
        this.capability = capability;
    }

    @Override
    public String filter(String value) {
        return value.toUpperCase().trim();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Optional<String> getCapability() {
        return capability;
    }

    @Override
    public void setCapability(Optional<String> capability) {
        this.capability = capability;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("capability", capability)
                .toString();
    }
}


