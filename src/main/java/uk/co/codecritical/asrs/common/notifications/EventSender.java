package uk.co.codecritical.asrs.common.notifications;

import java.util.LinkedList;
import java.util.Queue;

public class EventSender<N extends Notification> {
    private final Queue<Listener<N>> queue = new LinkedList<>();

    public EventSender() {
        NotificationRouter.add(this);
    }

    public void addListener(Listener<N> listener) {
        synchronized (queue) {
            queue.add(listener);
        }
    }

    public void sendNotification(N n) {
        synchronized (queue) {
            queue.forEach(l -> l.handleNotification(n));
        }
    }

    void clear() {
        synchronized (queue) {
            queue.clear();
        }
    }
}
