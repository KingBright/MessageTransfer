package name.kingbright.messagetransfer.core.models;

/**
 * @author Jin Liang
 * @since 2017/4/15
 */

public enum Type {
    Bind(21),
    BindResponse(22),
    Sms(31),
    SmsResponse(32);

    private final int code;

    Type(int i) {
        this.code = i;
    }

    public int getCode() {
        return code;
    }

    public static Type fromCode(int code) {
        for (Type type : Type.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
