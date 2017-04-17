package name.kingbright.messagetransfer.core;

import android.content.Context;

import name.kingbright.messagetransfer.core.models.BaseMessage;
import name.kingbright.messagetransfer.core.models.BindMessage;
import name.kingbright.messagetransfer.core.models.BindResponseMessage;
import name.kingbright.messagetransfer.core.models.SmsMessage;
import name.kingbright.messagetransfer.core.models.SmsResponseMessage;
import name.kingbright.messagetransfer.core.models.Source;
import name.kingbright.messagetransfer.core.models.Type;
import name.kingbright.messagetransfer.core.models.WrapperMessage;
import name.kingbright.messagetransfer.util.Base64Util;
import name.kingbright.messagetransfer.util.JsonUtil;
import name.kingbright.messagetransfer.util.SystemUtil;

/**
 * @author Jin Liang
 * @since 2017/4/16
 */

public class MessageFactory {
    private static MessageFactory instance;
    private final Context mContext;

    private MessageFactory(Context context) {
        mContext = context;
    }

    public SmsMessage buildSmsMessage(String sender, String body, long time) {
        SmsMessage smsMessage = new SmsMessage();
        appendBasicInfo(smsMessage);
        smsMessage.sender = sender;
        smsMessage.body = body;
        smsMessage.sign = Base64Util.endcode(sender + ":" + body);
        smsMessage.time = time;
        return smsMessage;
    }

    public WrapperMessage wrap(BaseMessage message) {
        WrapperMessage wrapperMessage = getWrapperMessage(Type.Sms);
        wrapperMessage.message = JsonUtil.toJson(message);
        return wrapperMessage;
    }

    public String wrapToString(BaseMessage message) {
        WrapperMessage wrapperMessage = getWrapperMessage(Type.Sms);
        wrapperMessage.message = JsonUtil.toJson(message);
        return JsonUtil.toJson(wrapperMessage);
    }

    public BindMessage buildBindMessage(String weiXinId) {
        BindMessage bindMessage = new BindMessage();
        appendBasicInfo(bindMessage);
        bindMessage.weiXinId = weiXinId;
        return bindMessage;
    }

    public WrapperMessage buildFromSocketMessage(SocketMessage socketMessage) {
        WrapperMessage wrapperMessage = JsonUtil.fromJson(socketMessage.message, WrapperMessage.class);
        return wrapperMessage;
    }

    public SmsResponseMessage getSmsResponseMessage(WrapperMessage wrapperMessage) {
        Type type = Type.fromCode(wrapperMessage.type);
        if (type == Type.SmsResponse) {
            return JsonUtil.fromJson(wrapperMessage.message, SmsResponseMessage.class);
        }
        return null;
    }

    public BindResponseMessage getBindResponseMessage(WrapperMessage wrapperMessage) {
        Type type = Type.fromCode(wrapperMessage.type);
        if (type == Type.BindResponse) {
            return JsonUtil.fromJson(wrapperMessage.message, BindResponseMessage.class);
        }
        return null;
    }

    private WrapperMessage getWrapperMessage(Type type) {
        WrapperMessage message = new WrapperMessage();
        message.type = type.getCode();
        message.source = Source.Phone.getCode();
        return message;
    }

    public static MessageFactory getInstance(Context context) {
        if (instance == null) {
            instance = new MessageFactory(context);
        }
        return instance;
    }

    private void appendBasicInfo(BaseMessage message) {
        message.model = SystemUtil.getPhoneModel();
        message.did = SystemUtil.getDeviceId(mContext);
        message.phone = SystemUtil.getPhoneNumber(mContext);
    }
}
