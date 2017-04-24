package name.kingbright.messagetransfer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import name.kingbright.cardlist.OnDismissCallback;
import name.kingbright.cardlist.RecyclerItemClickListener;
import name.kingbright.cardlist.card.Card;
import name.kingbright.cardlist.view.CardListAdapter;
import name.kingbright.cardlist.view.CardListView;
import name.kingbright.messagetransfer.R;
import name.kingbright.messagetransfer.core.InboxSmsReader;
import name.kingbright.messagetransfer.core.models.SmsMessage;
import name.kingbright.messagetransfer.ui.card.SmsCardProvider;
import name.kingbright.messagetransfer.util.L;

/**
 * Created by jinliang on 2017/4/20.
 */

public class MessageListFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "MessageListFragment";
    private CardListAdapter mListAdapter;
    private RecyclerItemClickListener.OnItemClickListener mOnItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(@NonNull Card card, int position) {

        }

        @Override
        public void onItemLongClick(@NonNull Card card, int position) {

        }
    };

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
        listView.addOnItemClickListener(mOnItemClickListener);
        listView.setOnDismissCallback(mOnItemDismissListener);

        mListAdapter = listView.getAdapter();

        List<SmsMessage> messages = readSms();
        L.d(TAG, "message list size : " + messages.size());
        for (SmsMessage message : messages) {
            Card card = new Card.Builder().withProvider(new SmsCardProvider(message)).build();

            mListAdapter.add(card);
        }
    }

    private List<SmsMessage> readSms() {
        InboxSmsReader reader = new InboxSmsReader(getContext());
        List<SmsMessage> list = reader.getSmsInboxWithLimit(20);
        return list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
