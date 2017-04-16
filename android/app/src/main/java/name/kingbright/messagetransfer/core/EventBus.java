package name.kingbright.messagetransfer.core;

/**
 * @author Jin Liang
 * @since 2017/4/16
 */

public class EventBus {
    public static void publish(Object event) {
        de.greenrobot.event.EventBus.getDefault().post(event);
    }

    public static void subscribe(Object subscriber) {
        try {
            de.greenrobot.event.EventBus.getDefault().register(subscriber);
        } catch (Throwable throwable) {

        }
    }

    public static void unsubscribe(Object subscriber) {
        try {
            de.greenrobot.event.EventBus.getDefault().unregister(subscriber);
        } catch (Throwable throwable) {

        }
    }
}
