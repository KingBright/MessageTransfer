package name.kingbright.messagetransfer.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;

/**
 * Created by jinliang on 2017/4/13.
 */

public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (isValidAction(action)) {
            startService(context, intent);
        }
    }

    private void startService(Context context, Intent intent) {
        Intent serviceIntent = new Intent();
        serviceIntent.setAction(Intents.ACTION_MESSAGE_TRANSFER);
        serviceIntent.putExtra("intent", intent);
        context.startService(serviceIntent);
    }

    private boolean isValidAction(String action) {
        return Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(action) || Telephony.Sms.Intents
                .DATA_SMS_RECEIVED_ACTION.equals(action);
    }
}
