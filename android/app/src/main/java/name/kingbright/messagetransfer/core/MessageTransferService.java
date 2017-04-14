package name.kingbright.messagetransfer.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;

import name.kingbright.messagetransfer.core.models.NormalMessage;
import name.kingbright.messagetransfer.util.JsonUtil;
import name.kingbright.messagetransfer.util.L;
import name.kingbright.messagetransfer.util.SystemUtil;

/**
 * Created by jinliang on 2017/4/13.
 */

public class MessageTransferService extends Service {
    private static final String TAG = "MessageTransferService";
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
    }

    private void syncMessage(SmsMessage sms) {
        String message = transformToSocketMessage(sms);
        mWebSocketManager.sendMessage(message);
    }

    private String transformToSocketMessage(SmsMessage sms) {
        NormalMessage message = new NormalMessage();
        message.model = SystemUtil.getPhoneModel();
        message.deviceId = SystemUtil.getDeviceId(getContext());
        message.phone = SystemUtil.getPhoneNumber(getContext());

        message.sender = sms.getDisplayOriginatingAddress();
        message.body = sms.getDisplayMessageBody();

        String json = JsonUtil.toJson(message);
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
