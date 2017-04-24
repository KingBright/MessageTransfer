package name.kingbright.cardlist.card;

import android.support.annotation.NonNull;

import name.kingbright.cardlist.card.provider.CardProvider;

/**
 * A basic Card.
 */
public class Card {

    @NonNull
    private CardProvider mProvider;

    /**
     * Creates a new Card.
     */
    private Card(CardProvider holder) {
        mProvider = holder;
    }

    /**
     * Get the card content.
     *
     * @return the card content.
     */
    @NonNull
    public CardProvider getProvider() {
        return mProvider;
    }

    /**
     * Weather it can be removed
     *
     * @return
     */
    public boolean isDismissible() {
        return mProvider.isDismissible();
    }

    public static class Builder<T> {
        private CardProvider<T> provider;

        public Builder() {

        }

        public Builder withProvider(CardProvider<T> provider) {
            this.provider = provider;
            return this;
        }

        public Card build() {
            return new Card(provider);
        }

    }
}
