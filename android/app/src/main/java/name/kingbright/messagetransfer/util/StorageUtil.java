package name.kingbright.messagetransfer.util;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

/**
 * Created by jinliang on 2017/4/25.
 */

public class StorageUtil {
    public static void init(Context context) {
        Hawk.init(context).build();
    }

    public static <T> void put(String key, T value) {
        Hawk.put(key, value);
    }

    public static <T> T get(String key, T defaultValue) {
        return Hawk.get(key, defaultValue);
    }

    public static boolean delete(String key) {
        return Hawk.delete(key);
    }

    public static boolean contains(String key) {
        return Hawk.contains(key);
    }

}
