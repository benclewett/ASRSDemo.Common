package uk.co.codecritical.asrs.common.notifications;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventRouterTest {

    @Test
    void testSingleListener() {
        MyListener listener = new MyListener();

        EventSender<MyNotification> eventSender = new EventSender<>();

        eventSender.addListener(listener);

        var notification = new MyNotification("Test");

        eventSender.sendNotification(notification);

        Assertions.assertEquals(notification.message, listener.message);
    }

    @Test
    void testDoubleListener() {
        MyListener listener = new MyListener();
        MyListener2 listener2 = new MyListener2();

        EventSender<MyNotification> eventSender = new EventSender<>();

        eventSender.addListener(listener);
        eventSender.addListener(listener2);

        var notification = new MyNotification("Test");

        eventSender.sendNotification(notification);

        Assertions.assertEquals(notification.message, listener.message);
        Assertions.assertEquals(notification.message, listener2.message);
    }

    static class MyNotification extends Notification {
        public final String message;
        public MyNotification(String message) {
            this.message = message;
        }
    }

    static class MyListener implements Listener<MyNotification> {
        public String message = null;

        @Override
        public void handleNotification(MyNotification notification) {
            this.message = notification.message;
        }
    }

    static class MyListener2 implements Listener<MyNotification> {
        public String message = null;

        @Override
        public void handleNotification(MyNotification notification) {
            this.message = notification.message;
        }
    }
}
