package com.dexafree.materialList.card;

import android.support.annotation.NonNull;

/**
 * A basic Card.
 */
public class Card {

    private boolean mDismissible;

    @NonNull
    private CardViewHolder mHolder;

    /**
     * Creates a new Card.
     */
    private Card() {
    }

    /**
     * @param holder to get the Card data from.
     */
    public void setViewHolder(CardViewHolder holder) {
        mHolder = holder;
    }

    /**
     * Get the card content.
     *
     * @return the card content.
     */
    @NonNull
    public CardViewHolder getViewHodler() {
        return mHolder;
    }

    /**
     * Weather it can be removed
     *
     * @return
     */
    public boolean isDismissible() {
        return mDismissible;
    }

    public static class Builder {

        public Builder() {

        }


    }
}
