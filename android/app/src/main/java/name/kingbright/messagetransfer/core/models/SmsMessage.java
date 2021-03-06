package name.kingbright.messagetransfer.core.models;

/**
 * Created by jinliang on 2017/4/14.
 */

public class SmsMessage extends BaseMessage {
    /**
     * 该消息的发送者
     */
    public String sender;
    /**
     * 该消息的消息内容
     */
    public String body;
    /**
     * 短信签名
     */
    public String sign;

    public long time;
}
