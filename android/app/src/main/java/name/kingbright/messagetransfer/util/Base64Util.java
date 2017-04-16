package name.kingbright.messagetransfer.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * @author Jin Liang
 * @since 2017/4/16
 */

public class Base64Util {
    public static String endcode(String txt) {
        try {
            byte[] bytes = txt.getBytes("UTF-8");
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
