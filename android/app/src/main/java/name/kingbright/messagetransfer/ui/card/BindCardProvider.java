package name.kingbright.messagetransfer.ui.card;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import name.kingbright.cardlist.card.provider.CardProvider;
import name.kingbright.messagetransfer.Constants;
import name.kingbright.messagetransfer.MessageTransferApplication;
import name.kingbright.messagetransfer.R;
import name.kingbright.messagetransfer.core.EventBus;
import name.kingbright.messagetransfer.core.Intents;
import name.kingbright.messagetransfer.core.MessageTransferService;
import name.kingbright.messagetransfer.core.models.BindResponseMessage;
import name.kingbright.messagetransfer.util.StorageUtil;
import name.kingbright.messagetransfer.util.ToastHelper;

/**
 * Created by jinliang on 2017/4/25.
 */

public class BindCardProvider extends CardProvider<BindCardProvider.BindInfo> {

    public BindCardProvider(BindInfo bindInfo) {
        super(bindInfo);
        EventBus.subscribe(this);
    }

    @Override
    public int getViewId() {
        return R.layout.go_bind_card;
    }

    @Override
    protected void bind(BindInfo data) {
        TextView status = getView(R.id.title);
        status.setText(data.title);

        TextView message = getView(R.id.message);
        message.setText(data.message);

        hindProgress();
    }

    @Override
    public void onClick() {
        if (getData().status == BindInfo.STATUS_UNBIND) {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.bind_title)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(R.string.bind_hint, 0, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                        }
                    }).positiveText(R.string.confirm_bind).onPositive
                    (new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String input = dialog.getInputEditText().getText().toString();
                            if (TextUtils.isEmpty(input)) {
                                ToastHelper.showToast(getContext(), R.string.weixin_id_is_empty);
                            } else {
                                StorageUtil.put(Constants.KEY_WEI_XIN_ID, input);

                                Context context = MessageTransferApplication.getContext();
                                Intent intent = new Intent(context, MessageTransferService.class);
                                intent.setAction(Intents.ACTION_BIND);
                                intent.putExtra(Intents.EXTRA_WEIXIN_ID, input);
                                context.startService(intent);

                                showProgress();
                            }
                        }
                    }).build().show();
        }
    }

    private void showProgress() {
        ContentLoadingProgressBar progressBar = getView(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hindProgress() {
        ContentLoadingProgressBar progressBar = getView(R.id.progress);
        progressBar.setVisibility(View.GONE);
    }

    public void onEventMainThread(BindResponseMessage message) {
        BindInfo info = getData();
        if (message.isSuccess()) {
            info.title = getContext().getString(R.string.bind_soon_please);
            info.message = getContext().getString(R.string.verification_code_is, message.code);
            info.status = BindInfo.STATUS_BINDING;
            StorageUtil.put(Constants.KEY_BIND_STATE, info);
        } else if (message.isBinded()) {
            info.title = getContext().getString(R.string.bind_success);
            info.message = getContext().getString(R.string.bind_success_message);
            info.status = BindInfo.STATUS_BINDED;
            StorageUtil.put(Constants.KEY_BIND_STATE, info);
        } else {
            info.title = getContext().getString(R.string.bind_fail);
            info.message = getContext().getString(R.string.bind_fail_message);
            info.status = BindInfo.STATUS_UNBIND;
        }
        bind(info);
    }

    public static class BindInfo {
        public static final int STATUS_UNBIND = 1;
        public static final int STATUS_BINDING = 2;
        public static final int STATUS_BINDED = 3;

        public int status = STATUS_UNBIND;
        public String title;
        public String message;
    }
}
