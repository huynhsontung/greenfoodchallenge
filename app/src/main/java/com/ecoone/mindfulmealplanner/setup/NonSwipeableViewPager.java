package com.ecoone.mindfulmealplanner.setup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import javax.annotation.Nonnull;

class NonSwipeableViewPager extends ViewPager{

    public NonSwipeableViewPager(@NonNull Context context) {
        super(context);
    }

    public NonSwipeableViewPager(@Nonnull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}