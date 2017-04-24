package name.kingbright.messagetransfer.ui.card;

import android.widget.TextView;

import name.kingbright.cardlist.card.provider.CardProvider;
import name.kingbright.messagetransfer.R;
import name.kingbright.messagetransfer.core.models.SmsMessage;
import name.kingbright.messagetransfer.util.TimeUtils;

/**
 * Created by jinliang on 2017/4/24.
 */

public class SmsCardProvider extends CardProvider<SmsMessage> {
    private static final java.lang.String TAG = "SmsCardProvider";

    public SmsCardProvider(SmsMessage message) {
        super(message);
    }

    @Override
    public int getViewId() {
        return R.layout.sms_card_layout;
    }

    @Override
    public boolean isDismissible() {
        return false;
    }

    protected void bind(SmsMessage data) {
        TextView title = getView(R.id.title);
        TextView content = getView(R.id.content);
        TextView time = getView(R.id.time);

        title.setText(data.sender);
        content.setText(data.body);
        time.setText(TimeUtils.format(data.time));
    }
}
