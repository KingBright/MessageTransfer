package name.kingbright.cardlist.card.event;

import android.support.annotation.NonNull;

import name.kingbright.cardlist.card.Card;

public class DismissEvent {
    private final Card mCard;

    public DismissEvent(@NonNull final Card card) {
        mCard = card;
    }

    public Card getCard() {
        return mCard;
    }
}
