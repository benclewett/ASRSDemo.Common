package uk.co.codecritical.asrs.common.notifications;

import java.util.LinkedList;
import java.util.Queue;

public class EventSender<N extends Notification> {
    private final Queue<Listener<N>> queue = new LinkedList<>();

    public void addListener(Listener<N> listener) {
        queue.add(listener);
    }

    public void sendNotification(N n) {
        queue.forEach(l -> l.handleNotification(n));
    }
}
