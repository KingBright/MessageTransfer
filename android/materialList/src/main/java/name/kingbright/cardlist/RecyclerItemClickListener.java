package name.kingbright.cardlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;

import name.kingbright.cardlist.card.Card;

// From http://stackoverflow.com/a/26196831/1610001
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private RecyclerView mRecyclerView;

    public interface OnItemClickListener {
        void onItemClick(@NonNull final Card card, int position);

        void onItemLongClick(@NonNull final Card card, int position);
    }

    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                CardView childView = (CardView) mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                //TODO
//                if (childView != null && mListener != null) {
//                    mListener.onItemLongClick(childView.getCard(), mRecyclerView.getChildPosition(childView));
//                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        CardView childView = (CardView) view.findChildViewUnder(e.getX(), e.getY());

        //TODO
//        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
//            mListener.onItemClick(childView.getCard(), view.getChildPosition(childView));
//        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}