package name.kingbright.messagetransfer.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;

import name.kingbright.messagetransfer.Constants;
import name.kingbright.messagetransfer.core.models.BindMessage;
import name.kingbright.messagetransfer.core.models.BindResponseMessage;
import name.kingbright.messagetransfer.core.models.SmsMessage;
import name.kingbright.messagetransfer.core.models.SmsResponseMessage;
import name.kingbright.messagetransfer.core.models.Type;
import name.kingbright.messagetransfer.core.models.WrapperMessage;
import name.kingbright.messagetransfer.db.SmsOpenHelper;
import name.kingbright.messagetransfer.util.L;

/**
 * Created by jinliang on 2017/4/13.
 */

public class MessageTransferService extends Service {
    private static final String TAG = "MessageTransferService";
    private AbsWebSocketManager mWebSocketManager;
    private MessageFactory mMessageFactory;

    private SmsOpenHelper mSmsOpenHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMessageFactory = MessageFactory.getInstance(getContext());
        mSmsOpenHelper = SmsOpenHelper.getInstance(getContext());

        mWebSocketManager = new WebSocketManagerImpl();
        mWebSocketManager.setUrl(Constants.WSS_SERVER);
        mWebSocketManager.start();

        EventBus.subscribe(this);
    }

    public void onEventMainThread(SocketMessage message) {
        L.d(TAG, "received from server");
        WrapperMessage wrapperMessage = mMessageFactory.buildFromSocketMessage(message);
        Type type = Type.fromCode(wrapperMessage.type);
        if (type == Type.BindResponse) {
            BindResponseMessage bindMessage = mMessageFactory.getBindResponseMessage(wrapperMessage);
            if (bindMessage.isSuccess()) {
                L.d(TAG, "verification code is : " + bindMessage.code);
            } else {
                L.d(TAG, "verification msg : " + bindMessage.msg);
            }
        } else if (type == Type.SmsResponse) {
            SmsResponseMessage smsResponseMessage = mMessageFactory.getSmsResponseMessage(wrapperMessage);
            if (smsResponseMessage.isSuccess()) {
                L.d(TAG, "sms confirmed : " + smsResponseMessage.sign);
                mSmsOpenHelper.updateToSynced(smsResponseMessage.sign);
            } else {
                L.d(TAG, "sms error message : " + smsResponseMessage.msg);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        String action = intent.getAction();
        if (Intents.ACTION_MESSAGE_TRANSFER.equals(action)) {
            Intent smsIntent = intent.getParcelableExtra(Intents.EXTRA_INTENT);
            if (smsIntent != null) {
                handleSmsIntent(smsIntent);
            }
        } else if (Intents.ACTION_BIND.equals(action)) {
            String weiXinId = intent.getStringExtra(Intents.EXTRA_WEIXIN_ID);
            doBind(weiXinId);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void doBind(String weiXinId) {
        BindMessage message = mMessageFactory.buildBindMessage(weiXinId);
        mWebSocketManager.sendMessage(mMessageFactory.wrapToString(message));
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
        SmsMessage message = transformToSocketMessage(sms);
        mSmsOpenHelper.insertOrUpdate(message);
        String json = mMessageFactory.wrapToString(message);
        L.d(TAG, "message : " + json);
        mWebSocketManager.sendMessage(json);
    }

    private SmsMessage transformToSocketMessage(android.telephony.SmsMessage sms) {
        SmsMessage message = mMessageFactory.buildSmsMessage(sms.getDisplayOriginatingAddress(), sms
                .getDisplayMessageBody(), sms.getTimestampMillis());
        message.sender = InboxSmsReader.getSenderNameByNumber(getContentResolver(), message.sender);
        return message;
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
