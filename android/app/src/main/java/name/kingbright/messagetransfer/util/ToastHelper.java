package name.kingbright.messagetransfer.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by jinliang on 2017/5/6.
 */

public class ToastHelper {

    public static void showToast(Context context, @StringRes int id) {
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }
}
