package name.kingbright.messagetransfer.core;

/**
 * @author Jin Liang
 * @since 2017/4/17
 */

interface IWebSocketManager {
    enum State {
        Waiting,
        Success,
        Fail,
        Close
    }

    boolean isSuccess();

    boolean isWaiting();

    void start();

    void stop();

    boolean sendMessage(String message);

    interface WebSocketListener {
        void onOpen();

        void onClose();

        void onError();

        void onMessage(String message);
    }
}
