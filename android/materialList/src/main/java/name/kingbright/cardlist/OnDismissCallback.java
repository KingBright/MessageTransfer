package name.kingbright.cardlist;

import android.support.annotation.NonNull;

import name.kingbright.cardlist.card.Card;

/**
 * The OnDismissCallback will be notified if a Card is dismissed.
 */
public interface OnDismissCallback {
    /**
     * A Card is dismissed.
     *
     * @param card
     *         which is dismissed.
     * @param position
     *         where the Card is.
     */
    void onDismiss(@NonNull final Card card, int position);
}
