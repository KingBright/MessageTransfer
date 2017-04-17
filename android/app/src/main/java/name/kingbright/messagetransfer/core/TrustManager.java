package name.kingbright.messagetransfer.core;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class TrustManager implements X509TrustManager {

    private static final X509Certificate[] sAcceptedCertificates = new X509Certificate[]{};

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {

    }


    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return sAcceptedCertificates;
    }

    public static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public static SSLSocketFactory getSSLSocketFactory() {

        SSLSocketFactory factory = null;

        javax.net.ssl.TrustManager[] trustManagers = new javax.net.ssl.TrustManager[]{
                new TrustManager()
        };

        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
            factory = context.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

//        HttpsURLConnection.setDefaultSSLSocketFactory(factory);
        return factory;
    }
}
