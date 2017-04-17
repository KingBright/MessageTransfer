package name.kingbright.messagetransfer.core;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

/**
 * @author Jin Liang
 * @since 2017/4/17
 */

public class WebSocketManagerImpl extends AbsWebSocketManager {

    private WebSocket mWebSocket;

    public WebSocketManagerImpl() {
        try {
            TrustManager[] trustManagers = new TrustManager[]{
                    new TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
            AsyncHttpClient.getDefaultInstance().getSSLSocketMiddleware().setSSLContext(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        AsyncHttpClient.getDefaultInstance().websocket(getUrl(),
                "wss", new AsyncHttpClient.WebSocketConnectCallback() {
                    @Override
                    public void onCompleted(Exception ex, WebSocket webSocket) {
                        if (ex != null) {
                            notifyError();
                            return;
                        }
                        mWebSocket = webSocket;
                        notifyOpen();
                        webSocket.setClosedCallback(new CompletedCallback() {
                            @Override
                            public void onCompleted(Exception ex) {
                                notifyClose();
                                mWebSocket = null;
                            }
                        });
                        webSocket.setStringCallback(new WebSocket.StringCallback() {
                            @Override
                            public void onStringAvailable(String message) {
                                notifyMessage(message);
                            }
                        });
                    }
                });
    }

    @Override
    protected void sendMessageInner(String message) {
        mWebSocket.send(message);
    }

    @Override
    public void stop() {
        if (mWebSocket != null) {
            mWebSocket.close();
        }
    }
}
