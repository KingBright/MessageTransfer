package name.kingbright.messagetransfer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.ListCardProvider;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListAdapter;
import com.dexafree.materialList.view.MaterialListView;

import name.kingbright.messagetransfer.R;

/**
 * Created by jinliang on 2017/4/20.
 */

public class InfoListFragment extends android.support.v4.app.Fragment {

    private MaterialListAdapter mListAdapter;
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
        MaterialListView mListView = (MaterialListView) view.findViewById(R.id.card_list);
        mListView.addOnItemClickListener(mOnItemClickListener);
        mListView.setOnDismissCallback(mOnItemDismissListener);
        mListAdapter = mListView.getAdapter();

        Card card = new Card.Builder(getContext())
                .setTag("BASIC_IMAGE_BUTTONS_CARD")
                .withProvider(new ListCardProvider())
                .build();

        mListAdapter.add(card);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
