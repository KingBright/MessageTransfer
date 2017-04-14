package name.kingbright.messagetransfer.core;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;

/**
 * Created by jinliang on 2017/4/13.
 */

public class MessageTransferService extends Service {

    private WebSocketManager mWebSocketManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWebSocketManager = new WebSocketManager();
        mWebSocketManager.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent smsIntent = intent.getParcelableExtra("intent");
        if (smsIntent != null) {
            handleIntent(smsIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void handleIntent(Intent intent) {
        SmsMessage[] messages;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            messages = getMessagesFromIntent(intent);
        } else {
            messages = getMessagesFromIntent(intent);
        }

        if (messages != null && messages.length != 0) {
            for (SmsMessage sms : messages) {
                if (sms != null) {
                    syncMessage(sms);
                }
            }
        }
    }

    private void syncMessage(SmsMessage sms) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebSocketManager.stop();
    }

    /**
     * Read the PDUs out of an {@link Telephony.Sms.Intents#SMS_RECEIVED_ACTION} or a
     * {@link Telephony.Sms.Intents#DATA_SMS_RECEIVED_ACTION} intent.
     *
     * @param intent the intent to read from
     * @return an array of SmsMessages for the PDUs
     */
    private SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        int pduCount = messages.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            byte[] pdu = (byte[]) messages[i];
            msgs[i] = SmsMessage.createFromPdu(pdu);
        }
        return msgs;
    }
}
