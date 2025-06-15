package uk.co.codecritical.asrs.common.notifications;

import com.google.common.base.MoreObjects;

public class NotificationDebug extends Notification {
    public final String message;
    public NotificationDebug(String message) {
        super(Level.DEBUG);
        this.message = message;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("level", level)
                .add("message", message)
                .toString();
    }

    public static final EventSender<NotificationDebug> DEBUG_NOTIFICATION_EVENT_SENDER = new EventSender<>();
}

