package name.kingbright.messagetransfer.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;

import name.kingbright.messagetransfer.core.models.SmsMessage;
import name.kingbright.messagetransfer.core.models.WrapperMessage;
import name.kingbright.messagetransfer.util.L;

/**
 * Created by jinliang on 2017/4/13.
 */

public class MessageTransferService extends Service {
    private static final String TAG = "MessageTransferService";
    private WebSocketManager mWebSocketManager;
    private MessageFactory mMessageFactory;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMessageFactory = MessageFactory.getInstance(getContext());
        mWebSocketManager = new WebSocketManager();
        mWebSocketManager.start();

        EventBus.subscribe(this);
    }

    public void onEventMainThread(SocketMessage message) {
        L.d(TAG, "received from server");
        WrapperMessage wrapperMessage = mMessageFactory.buildFromSocketMessage(message);
        L.d(TAG, wrapperMessage.type);
        L.d(TAG, wrapperMessage.message);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        String action = intent.getAction();
        if (Intents.ACTION_MESSAGE_TRANSFER.equals(action)) {
            Intent smsIntent = intent.getParcelableExtra("intent");
            if (smsIntent != null) {
                handleSmsIntent(smsIntent);
            }
        } else if (Intents.ACTION_REGISTER.equals(action)) {
            doRegister();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void doRegister() {

    }

    private boolean isValidAction(String action) {
        return Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(action) || Telephony.Sms.Intents
                .DATA_SMS_RECEIVED_ACTION.equals(action);
    }

    private void handleSmsIntent(Intent intent) {
        String action = intent.getAction();
        if (isValidAction(action)) {
            android.telephony.SmsMessage[] messages;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                messages = getMessagesFromIntent(intent);
            } else {
                messages = getMessagesFromIntent(intent);
            }

            if (messages != null && messages.length != 0) {
                for (android.telephony.SmsMessage sms : messages) {
                    if (sms != null) {
                        syncMessage(sms);
                    }
                }
            }
        }
    }

    private void syncMessage(android.telephony.SmsMessage sms) {
        String message = transformToSocketMessage(sms);
        mWebSocketManager.sendMessage(message);
    }

    private String transformToSocketMessage(android.telephony.SmsMessage sms) {
        SmsMessage message = mMessageFactory.buildSmsMessage(sms.getDisplayOriginatingAddress(), sms
                .getDisplayMessageBody(), sms.getTimestampMillis());

        String json = mMessageFactory.wrapToString(message);
        L.d(TAG, "message : " + json);
        return json;
    }

    private Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebSocketManager.stop();
        EventBus.unsubscribe(this);
    }

    /**
     * Read the PDUs out of an {@link Telephony.Sms.Intents#SMS_RECEIVED_ACTION} or a
     * {@link Telephony.Sms.Intents#DATA_SMS_RECEIVED_ACTION} intent.
     *
     * @param intent the intent to read from
     * @return an array of SmsMessages for the PDUs
     */
    private android.telephony.SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        int pduCount = messages.length;
        android.telephony.SmsMessage[] msgs = new android.telephony.SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            byte[] pdu = (byte[]) messages[i];
            msgs[i] = android.telephony.SmsMessage.createFromPdu(pdu);
        }
        return msgs;
    }
}
