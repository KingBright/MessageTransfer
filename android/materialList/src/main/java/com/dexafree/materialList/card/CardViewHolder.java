package com.dexafree.materialList.card;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Provide view and content and other customizable things.
 */
public abstract class CardViewHolder extends RecyclerView.ViewHolder {
    public CardViewHolder(View view) {
        super(view);
    }

    /**
     * 获取该卡片的 layout
     *
     * @return
     */
    @LayoutRes
    public abstract int getViewId();

    /**
     * 是否可移除
     *
     * @return
     */
    public abstract boolean isDismissible();
}
