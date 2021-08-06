package com.yzj.eplorer;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

public class SwipeController extends ItemTouchHelper.Callback {

    boolean swipeBack =false;

    @Override
    public int getMovementFlags(RecyclerView p1, RecyclerView.ViewHolder p2) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView p1, RecyclerView.ViewHolder p2, RecyclerView.ViewHolder p3) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder p1, int p2) {
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
           setOnTouch(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void setOnTouch(final Canvas c, final RecyclerView rv, final RecyclerView.ViewHolder holder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        rv.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View p1, MotionEvent e) {
                    swipeBack=e.getAction()==MotionEvent.ACTION_CANCEL||e.getAction()==MotionEvent.ACTION_UP;
                   // if(listener!=null && swipeBack && Math.abs(dX-dY)>0){
					if(listener!=null && swipeBack){
						listener.onSwipe(holder.getAdapterPosition(),dX,dY);
					}
					return false;
                }
            });
    }

    OnSwipeListener listener;
    public void setOnSwipeListener(OnSwipeListener listener){
        this.listener=listener;
    }
    public interface OnSwipeListener{
        void onSwipe(int position,float dx,float dy);
    }
}
