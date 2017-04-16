package name.kingbright.messagetransfer.util;

import android.util.Log;

/**
 * Created by jinliang on 2017/4/14.
 */

public class L {

    private static final String DEFAULT_TAG = "DEBUG";

    public static void d(String message) {
        d(DEFAULT_TAG, message);
    }

    public static void d(String tag, Object message) {
        if (message != null) {
            Log.d(tag, message.toString());
        } else {
            Log.d(tag, "##EmptyMessage##");
        }
    }
}
