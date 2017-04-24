package name.kingbright.messagetransfer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jinliang on 2017/4/25.
 */

public class TimeUtils {
    public static String format(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        return sdf.format(new Date(time));
    }
}
