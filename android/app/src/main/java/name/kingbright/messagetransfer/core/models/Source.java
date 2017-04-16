package name.kingbright.messagetransfer.core.models;

/**
 * @author Jin Liang
 * @since 2017/4/16
 */

public enum Source {
    WeiXin(1),
    Phone(2);

    private final int code;

    Source(int i) {
        this.code = i;
    }

    public int getCode() {
        return code;
    }
}
