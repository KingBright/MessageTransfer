package name.kingbright.messagetransfer;

import android.app.Application;

import name.kingbright.messagetransfer.util.StorageUtil;

/**
 * Created by jinliang on 2017/4/25.
 */

public class MessageTransferApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StorageUtil.init(getApplicationContext());
    }
}
