package name.kingbright.messagetransfer.core.models;

import android.text.TextUtils;

/**
 * @author Jin Liang
 * @since 2017/4/16
 */

public class BindResponseMessage {
    /**
     * 验证码
     */
    public String code;
    public String msg;

    public boolean isSuccess() {
        return !TextUtils.isEmpty(code) && code.length() == 4;
    }

    public boolean isFail() {
        return "-1".equals(code);
    }

    public boolean isBinded() {
        return "0".equals(code);
    }
}
