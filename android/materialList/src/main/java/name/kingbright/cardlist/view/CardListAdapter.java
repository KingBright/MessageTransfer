package name.kingbright.cardlist.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import name.kingbright.cardlist.card.Card;
import name.kingbright.cardlist.card.event.DismissEvent;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder>
        implements Observer {
    private final CardListView.OnSwipeAnimation mSwipeAnimation;
    private final CardListView.OnAdapterItemsChanged mItemAnimation;
    private final List<Card> mCardList = new ArrayList<>();

    public CardListAdapter(@NonNull final CardListView.OnSwipeAnimation swipeAnimation,
                           @NonNull final CardListView.OnAdapterItemsChanged itemAnimation) {
        mSwipeAnimation = swipeAnimation;
        mItemAnimation = itemAnimation;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View contentView;

        public ViewHolder(CardView cardView, View contentView) {
            super(cardView);
            this.contentView = contentView;
        }

        public void build(Card card) {
            if (card != null && card.getProvider() != null) {
                card.getProvider().bindView(contentView);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewId) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardView cardView = (CardView) inflater.inflate(R.layout.card_layout, parent, false);
        View contentView = inflater.inflate(viewId, cardView, true);
        return new ViewHolder(cardView, contentView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.build(getCard(position));
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }

    @Override
    public int getItemViewType(final int position) {
        Card card = getCard(position);
        if (card != null) {
            return card.getProvider().getViewId();
        }
        return 0;
    }

    /**
     * Add a Card at a specific position with or without a scroll animation.
     *
     * @param position of the card to insert.
     * @param card     to insert.
     * @param scroll   will trigger an animation if it is set to <code>true</code> otherwise not.
     */
    public void add(final int position, @NonNull final Card card, final boolean scroll) {
        mCardList.add(position, card);
        mItemAnimation.onAddItem(position, scroll);
        notifyItemInserted(position); // Triggers the animation!
    }

    /**
     * Add a Card at a specific position.
     *
     * @param position of the card to insert.
     * @param card     to insert.
     */
    public void add(final int position, @NonNull final Card card) {
        add(position, card, true);
    }

    /**
     * Add a Card at the start.
     *
     * @param card to add at the start.
     */
    public void addAtStart(@NonNull final Card card) {
        add(0, card);
    }

    /**
     * Add a Card.
     *
     * @param card to add.
     */
    public void add(@NonNull final Card card) {
        add(mCardList.size(), card);
    }

    /**
     * Add all Cards.
     *
     * @param cards to add.
     */
    public void addAll(@NonNull final Card... cards) {
        addAll(Arrays.asList(cards));
    }

    /**
     * Add all Cards.
     *
     * @param cards to add.
     */
    public void addAll(@NonNull final Collection<Card> cards) {
        int index = 0;
        for (Card card : cards) {
            add(index++, card, false);
        }
    }

    public void remove(@NonNull final Card card, boolean animate) {
        remove(card, animate, false);
    }

    /**
     * Remove a Card withProvider or without an animation.
     *
     * @param card    to remove.
     * @param animate {@code true} to animate the remove process or {@code false} otherwise.
     */
    public void remove(@NonNull final Card card, boolean animate, boolean force) {
        if (force || card.isDismissible()) {
            if (animate) {
                mSwipeAnimation.animate(getPosition(card));
            } else {
                mCardList.remove(card);
                mItemAnimation.onRemoveItem();
                notifyDataSetChanged();
            }
        }
    }

    /**
     * Clears the list from all Cards (even if they are not dismissible).
     */
    public void forceClear() {
        while (!mCardList.isEmpty()) {
            final Card card = mCardList.get(0);
            remove(card, false, true);
            notifyItemRemoved(0);
        }
    }

    /**
     * Clears the list from all Cards (only if dismissible).
     */
    public void clear() {
        for (int index = 0; index < mCardList.size(); ) {
            final Card card = mCardList.get(index);
            if (!card.isDismissible()) {
                index++;
            }
            remove(card, false);
            notifyItemRemoved(index);
        }
    }

    /**
     * Is the list empty?
     *
     * @return {@code true} if the list is empty or {@code false} otherwise.
     */
    public boolean isEmpty() {
        return mCardList.isEmpty();
    }

    /**
     * Get a Card at the specified position.
     *
     * @param position of the Card.
     * @return the Card or {@code null} if the position is outside of the list range.
     */
    @Nullable
    public Card getCard(int position) {
        if (position >= 0 && position < mCardList.size()) {
            return mCardList.get(position);
        }
        return null;
    }

    /**
     * Get the position of a specified Card.
     *
     * @param card to get the position of.
     * @return the position.
     */
    public int getPosition(@NonNull Card card) {
        return mCardList.indexOf(card);
    }

    @Override
    public void update(final Observable observable, final Object data) {
        if (data instanceof DismissEvent) {
            remove(((DismissEvent) data).getCard(), true);
        }
        if (data instanceof Card) {
            notifyDataSetChanged();
        }
    }
}
