package uk.co.codecritical.asrs.common.notifications;

import java.util.ArrayList;
import java.util.List;

public class NotificationRouter {
    private static final List<EventSender<? extends Notification>> eventSenders = new ArrayList<>();

    /** Static class */
    private NotificationRouter() {
    }

    public static void add(EventSender<? extends Notification> eventSender) {
        eventSenders.add(eventSender);
    }

    public static void clearAll() {
        eventSenders.forEach(EventSender::clear);
    }
}
