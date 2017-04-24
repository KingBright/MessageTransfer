package name.kingbright.messagetransfer.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jinliang on 2017/4/24.
 */

public class MatcherUtil {

    private static final String regularExpression = "【(.*?)】";

    public static String findFirstMatch(String input) {
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

}
