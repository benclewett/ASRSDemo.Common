package uk.co.codecritical.asrs.common.notifications;

import com.google.common.base.MoreObjects;

public class NotificationWarning extends Notification  {
    public final String message;
    public NotificationWarning(String message) {
        super(Level.WARNING);
        this.message = message;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("level", level)
                .add("message", message)
                .toString();
    }

    public static final EventSender<NotificationWarning> WARNING_NOTIFICATION_EVENT_SENDER = new EventSender<>();
}
