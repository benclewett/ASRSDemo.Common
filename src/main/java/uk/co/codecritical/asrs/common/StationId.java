package uk.co.codecritical.asrs.common;

import com.google.common.base.MoreObjects;

public class StationId {
    public final int id;
    public final String capability;
    public StationId(int id, String capability) {
        this.id = id;
        this.capability = filter(capability);
    }
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("capability", capability)
                .toString();
    }
    public static String filter(String s) {
        return s.toUpperCase();
    }
}
