package uk.co.codecritical.asrs.common.notifications;

import java.util.ArrayList;
import java.util.List;

public class Router {
    private static final List<EventSender<? extends Notification>> eventSenders = new ArrayList<>();

    /** Static class */
    private Router() {
    }

    public static void add(EventSender<? extends Notification> eventSender) {
        eventSenders.add(eventSender);
    }

    public static void clearAll() {
        eventSenders.forEach(EventSender::clear);
    }
}
