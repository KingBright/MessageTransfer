package name.kingbright.messagetransfer.core.models;

/**
 * @author Jin Liang
 * @since 2017/4/16
 */

public class SmsResponseMessage {
    public int code;
    /**
     * 签名确认码
     */
    public String sign;
    public String msg;
}
