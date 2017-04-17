package name.kingbright.messagetransfer.core;

import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import name.kingbright.messagetransfer.util.L;

/**
 * Created by jinliang on 2017/4/13.
 */

public class WebSocketManager {

    private State mState = State.Waiting;

    private enum State {
        Waiting,
        Success,
        Fail
    }

    private SimpleWebSocketClient mClient;
    private static final String TAG = "WebSocketManager";

    private Queue<String> mMessage = new LinkedBlockingQueue<>();
    private SimpleWebSocketClient.WebSocketListener mWebSocketListener = new SimpleWebSocketClient
            .WebSocketListener() {
        @Override
        public void onOpen() {
            updateState(State.Success);
            String message;
            while ((message = mMessage.poll()) != null) {
                sendMessage(message);
            }
        }

        @Override
        public void onClose() {
            updateState(State.Fail);
        }

        @Override
        public void onError() {
            updateState(State.Fail);
        }

        @Override
        public void onMessage(String message) {
            EventBus.publish(new SocketMessage(message));
        }
    };

    private void updateState(State state) {
        synchronized (WebSocketManager.class) {
            mState = state;
        }
    }

    private State getState() {
        return mState;
    }

    private boolean isSuccess() {
        return mState == State.Success;
    }

    private boolean isWaiting() {
        return mState == State.Waiting;
    }

    public WebSocketManager() {
    }

    public void start() {
        try {
            L.d(TAG, "create new instance");
            mClient = new SimpleWebSocketClient(new URI("ws://172.23.167.112:8442"));
            mClient.setWebSocketListener(mWebSocketListener);
        } catch (URISyntaxException e) {
            Log.d(TAG, "fail to create socket");
            return;
        }
        if (!mClient.isOpen() && !mClient.isConnecting()) {
            Log.d(TAG, "start to connect");
            try {
                mClient.connect();
            } catch (IllegalStateException e) {
                mClient = null;
                mState = State.Fail;
                Log.d(TAG, "can't reuse, recreate later");
            }
        }
    }

    /**
     * Send a message immediately or later.
     *
     * @param message
     * @return True if immediately, otherwise false.
     */
    public boolean sendMessage(String message) {
        if (isWaiting()) {
            enqueue(message, false);
            return false;
        } else if (isSuccess()) {
            Log.d(TAG, "send immediately");
            sendMessageInner(message);
            return true;
        } else {
            enqueue(message, true);
            return false;
        }
    }

    private void sendMessageInner(String message) {
        try {
            mClient.send(message);
        } catch (Throwable throwable) {
            enqueue(message, false);
        }
    }

    private void enqueue(String message, boolean tryStart) {
        Log.d(TAG, "send later");
        mMessage.offer(message);
        if (tryStart) {
            start();
        }
    }

    public void stop() {
        if (mClient != null && !mClient.isClosed() && !mClient.isClosing()) {
            mClient.close();
        }
    }
}
