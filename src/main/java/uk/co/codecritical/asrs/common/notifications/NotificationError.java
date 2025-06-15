package uk.co.codecritical.asrs.common.notifications;

import com.google.common.base.MoreObjects;

public class NotificationError extends Notification {
    public final String message;
    public NotificationError(String message) {
        super(Level.ERROR);
        this.message = message;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("level", level)
                .add("message", message)
                .toString();
    }

    public static final EventSender<NotificationError> ERROR_NOTIFICATION_EVENT_SENDER = new EventSender<>();
}
