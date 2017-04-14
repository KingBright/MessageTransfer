package name.kingbright.messagetransfer.util;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.thomashaertel.device.identification.DeviceIdentityProvider;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by jinliang on 2017/4/14.
 */

public class SystemUtil {
    public static String getDeviceId(Context context) {
        DeviceIdentityProvider identityProvider = DeviceIdentityProvider.getInstance(context);
        return identityProvider.generateDeviceId();
    }

    public static String getPhoneModel() {
        return Build.MODEL;
    }


    public static String getPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            return telephonyManager.getLine1Number();
        }
        return "";
    }
}
