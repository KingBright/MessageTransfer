package name.kingbright.messagetransfer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import name.kingbright.cardlist.OnDismissCallback;
import name.kingbright.cardlist.card.Card;
import name.kingbright.cardlist.view.CardListAdapter;
import name.kingbright.cardlist.view.CardListView;
import name.kingbright.messagetransfer.Constants;
import name.kingbright.messagetransfer.R;
import name.kingbright.messagetransfer.core.EventBus;
import name.kingbright.messagetransfer.core.InboxSmsReader;
import name.kingbright.messagetransfer.core.Intents;
import name.kingbright.messagetransfer.core.MessageTransferService;
import name.kingbright.messagetransfer.core.models.BindResponseMessage;
import name.kingbright.messagetransfer.core.models.SmsMessage;
import name.kingbright.messagetransfer.ui.card.BindCardProvider;
import name.kingbright.messagetransfer.ui.card.SmsCardProvider;
import name.kingbright.messagetransfer.util.L;
import name.kingbright.messagetransfer.util.StorageUtil;

/**
 * Created by jinliang on 2017/4/20.
 */

public class MessageListFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "MessageListFragment";
    private CardListAdapter mListAdapter;

    private OnDismissCallback mOnItemDismissListener = new OnDismissCallback() {
        @Override
        public void onDismiss(@NonNull Card card, int position) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_list_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        CardListView listView = (CardListView) view.findViewById(R.id.card_list);
        listView.setOnDismissCallback(mOnItemDismissListener);

        mListAdapter = listView.getAdapter();

        List<SmsMessage> messages = readSms();
        L.d(TAG, "message list size : " + messages.size());
        List<Card> cardList = new ArrayList<>();
        for (SmsMessage message : messages) {
            Card card = new Card.Builder().withProvider(new SmsCardProvider(message)).build();
            cardList.add(card);
        }
        mListAdapter.addAll(cardList);

        checkBindState();
    }

    private void checkBindState() {
        Intent intent = new Intent(getContext(), MessageTransferService.class);
        intent.setAction(Intents.ACTION_BIND);
        getContext().startService(intent);
    }

    private List<SmsMessage> readSms() {
        InboxSmsReader reader = new InboxSmsReader(getContext());
        List<SmsMessage> list = reader.getSmsInboxWithLimit(20);
        return list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.subscribe(this);
    }

    public void onEventMainThread(BindResponseMessage message) {
        if (message.isUnbind()) {
            BindCardProvider.BindInfo bindInfo = StorageUtil.get(Constants.KEY_BIND_STATE, null);
            if (bindInfo == null) {
                bindInfo = new BindCardProvider.BindInfo();
                bindInfo.title = getString(R.string.status_not_bind);
                bindInfo.message = getString(R.string.click_to_bind);
                bindInfo.status = BindCardProvider.BindInfo.STATUS_UNBIND;
            }

            mListAdapter.add(0, new Card.Builder().withProvider(new BindCardProvider(bindInfo)).build());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.unsubscribe(this);
    }
}
