package name.kingbright.messagetransfer.core;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import name.kingbright.messagetransfer.util.L;
import name.kingbright.messagetransfer.util.SystemUtil;

/**
 * Created by jinliang on 2017/4/13.
 */

public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = "MessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        startService(context, intent);
    }

    private void startService(Context context, Intent intent) {
        L.d(TAG, "message received");
        if (SystemUtil.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            Intent serviceIntent = new Intent(context, MessageTransferService.class);
            serviceIntent.setAction(Intents.ACTION_MESSAGE_TRANSFER);
            serviceIntent.putExtra("intent", intent);
            context.startService(serviceIntent);
        }
    }

}
