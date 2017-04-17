package name.kingbright.messagetransfer.core;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jinliang on 2017/4/13.
 */

public abstract class AbsWebSocketManager implements IWebSocketManager {
    private static final String TAG = "WebSocketManager";

    private State mState = State.Waiting;

    private Queue<String> mMessage = new LinkedBlockingQueue<>();
    private WebSocketListener mWebSocketListener = new WebSocketListener() {
        @Override
        public void onOpen() {
            Log.d(TAG, "on open");
            updateState(State.Success);
            String message;
            while ((message = mMessage.poll()) != null) {
                sendMessage(message);
            }
        }

        @Override
        public void onClose() {
            Log.d(TAG, "on close");
            updateState(State.Close);
        }

        @Override
        public void onError() {
            Log.d(TAG, "on error");
            updateState(State.Fail);
        }

        @Override
        public void onMessage(String message) {
            Log.d(TAG, "message received : " + message);
            EventBus.publish(new SocketMessage(message));
        }
    };

    private String mUrl;

    public final void setUrl(String url) {
        mUrl = url;
    }

    public final String getUrl() {
        return mUrl;
    }

    protected final void notifyClose() {
        mWebSocketListener.onClose();
    }

    protected final void notifyError() {
        mWebSocketListener.onError();
    }

    protected final void notifyMessage(String message) {
        mWebSocketListener.onMessage(message);
    }

    protected final void notifyOpen() {
        mWebSocketListener.onOpen();
    }

    private void updateState(State state) {
        synchronized (AbsWebSocketManager.class) {
            mState = state;
        }
    }

    public final boolean isSuccess() {
        return mState == State.Success;
    }

    public final boolean isWaiting() {
        return mState == State.Waiting;
    }

    public AbsWebSocketManager() {
    }

    public abstract void start();

    /**
     * Send a message immediately or later.
     *
     * @param message
     * @return True if immediately, otherwise false.
     */
    public final boolean sendMessage(String message) {
        if (isWaiting()) {
            enqueue(message, false);
            return false;
        } else if (isSuccess()) {
            try {
                Log.d(TAG, "try send immediately");
                sendMessageInner(message);
                return true;
            } catch (Exception e) {
                notifyError();
                enqueue(message, false);
                return false;
            }
        } else {
            enqueue(message, true);
            return false;
        }
    }

    protected abstract void sendMessageInner(String message);

    protected final void enqueue(String message, boolean tryStart) {
        Log.d(TAG, "send later");
        mMessage.offer(message);
        if (tryStart) {
            start();
        }
    }

    public abstract void stop();
}
