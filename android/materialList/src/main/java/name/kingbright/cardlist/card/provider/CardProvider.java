package name.kingbright.cardlist.card.provider;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.dexafree.materialList.R;

import java.util.HashMap;

/**
 * Provide view and content and other customizable things.
 */
public abstract class CardProvider<T> {
    private T mData;

    private HashMap<Integer, View> mViewCache = new HashMap<>();

    private View mContentView;

    public CardProvider(T t) {
        mData = t;
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
    public boolean isDismissible() {
        return true;
    }

    /**
     * 获取数据
     *
     * @return
     */
    public T getData() {
        return mData;
    }

    public final void bindView(View contentView) {
        mContentView = contentView;

        Object viewCache = mContentView.getTag(R.id.view_cache);
        if (viewCache != null) {
            mViewCache = (HashMap) viewCache;
        }
        bind(mData);
        contentView.setTag(R.id.view_cache, mViewCache);
    }

    protected abstract void bind(T data);

    protected <K extends View> K getView(@IdRes int id) {
        if (mViewCache.containsKey(id)) {
            return (K) mViewCache.get(id);
        }

        K childView = (K) mContentView.findViewById(id);
        if (childView != null) {
            mViewCache.put(id, childView);
        }
        return childView;
    }

}
