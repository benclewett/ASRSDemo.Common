package uk.co.codecritical.asrs.common.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Notification {
    @JsonIgnore
    public final Level level;
    protected Notification(Level level) {
        this.level = level;
    }
}
