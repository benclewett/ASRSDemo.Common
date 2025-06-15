package uk.co.codecritical.asrs.common.notifications;

import com.google.common.base.MoreObjects;

public class NotificationInfo extends Notification {
    public final String message;
    public NotificationInfo(String message) {
        super(Level.INFO);
        this.message = message;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("level", level)
                .add("message", message)
                .toString();
    }

    public static final EventSender<NotificationInfo> INFO_NOTIFICATION_EVENT_SENDER = new EventSender<>();
}
