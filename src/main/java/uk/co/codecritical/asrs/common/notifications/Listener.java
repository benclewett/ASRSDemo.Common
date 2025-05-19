package uk.co.codecritical.asrs.common.notifications;

@FunctionalInterface
public interface Listener <N extends Notification> {
    void handleNotification(N notification);
}
