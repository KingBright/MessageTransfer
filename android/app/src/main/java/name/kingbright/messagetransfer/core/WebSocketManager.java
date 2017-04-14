package name.kingbright.messagetransfer.core;

import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jinliang on 2017/4/13.
 */

public class WebSocketManager {
    private SimpleWebSocketClient mClient;
    private static final String TAG = "WebSocketManager";

    private Queue<String> mMessage = new LinkedBlockingQueue<>();
    private SimpleWebSocketClient.WebSocketListener mWebSocketListener = new SimpleWebSocketClient
            .WebSocketListener() {
        @Override
        public void onOpen() {
            String message;
            while ((message = mMessage.poll()) != null) {
                sendMessage(message);
            }
        }

        @Override
        public void onClose() {

        }

        @Override
        public void onError() {

        }

        @Override
        public void onMessage(String message) {

        }
    };

    public WebSocketManager() {
    }

    public void start() {
        if (mClient == null) {
            try {
                mClient = new SimpleWebSocketClient(new URI("wss://ssl.kingbright.name:443"));
                mClient.setWebSocketListener(mWebSocketListener);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return;
            }
        }
        if (!mClient.isOpen() && !mClient.isConnecting()) {
            Log.d(TAG, "start to connect");
            mClient.connect();
        }
    }

    /**
     * Send a message immediately or later.
     *
     * @param message
     * @return True if immediately, otherwise false.
     */
    public boolean sendMessage(String message) {
        if (mClient.isOpen()) {
            Log.d(TAG, "send immediately");
            try {
                mClient.send(message);
            } catch (NotYetConnectedException e) {
                e.printStackTrace();
                enqueue(message);
                return false;
            }
            return true;
        } else {
            enqueue(message);
            return false;
        }
    }

    private void enqueue(String message) {
        Log.d(TAG, "send later");
        mMessage.offer(message);
        start();
    }

    public void stop() {
        if (mClient != null && !mClient.isClosed() && !mClient.isClosing()) {
            mClient.close();
        }
    }
}
