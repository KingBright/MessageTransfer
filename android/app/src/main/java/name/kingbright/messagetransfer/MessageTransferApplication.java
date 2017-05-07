package name.kingbright.messagetransfer;

import android.app.Application;
import android.content.Context;

import name.kingbright.messagetransfer.util.StorageUtil;

/**
 * Created by jinliang on 2017/4/25.
 */

public class MessageTransferApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        StorageUtil.init(context);
    }
}
