package name.kingbright.messagetransfer.core;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import name.kingbright.messagetransfer.util.L;

/**
 * Simple websocket client
 */
public class SimpleWebSocketClient extends WebSocketClient {

    private static final String TAG = "WebSocket";

    private WebSocketListener mWebSocketListener;

    public SimpleWebSocketClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        L.d(TAG, "new connection opened");
        if (mWebSocketListener != null) {
            mWebSocketListener.onOpen();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        L.d(TAG, "connection closed with code " + code + " with reason " + reason);
        if (mWebSocketListener != null) {
            mWebSocketListener.onClose();
        }
    }

    @Override
    public void onMessage(String message) {
        L.d(TAG, "received message: " + message);
        if (mWebSocketListener != null) {
            mWebSocketListener.onMessage(message);
        }
    }

    @Override
    public void onError(Exception ex) {
        L.d(TAG, "an error occurred:" + ex);
        if (mWebSocketListener != null) {
            mWebSocketListener.onError();
        }
    }

    public void setWebSocketListener(WebSocketListener listener) {
        mWebSocketListener = listener;
    }

    public interface WebSocketListener {
        void onOpen();

        void onClose();

        void onError();

        void onMessage(String message);
    }
}