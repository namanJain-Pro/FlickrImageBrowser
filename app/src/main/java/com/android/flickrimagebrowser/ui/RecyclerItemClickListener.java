package com.android.flickrimagebrowser.ui;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecyclerItemClickListen";

    interface OnClick {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private final OnClick mListener;
    private final GestureDetectorCompat mGestureDetectorCompat;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView,OnClick listener) {
        mListener = listener;
        mGestureDetectorCompat = new GestureDetectorCompat(context ,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(childView != null) {
                    Log.d(TAG, "onSingleTapUp: Single click listener");
                    mListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(childView != null) {
                    Log.d(TAG, "onLongPress: Long click listener");
                    mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull @NotNull RecyclerView rv, @NonNull @NotNull MotionEvent e) {
        if(mGestureDetectorCompat != null) {
            boolean result = mGestureDetectorCompat.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent result: " + result);
            return result;
        } else {
            return false;
        }
    }
}
