/*
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: angeldevil
 * Date: 16-4-11
 */
package com.dylan.baseui.library.widget.horizontalscrollablelist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.OverScroller;

import java.lang.reflect.Field;

/**
 * 可监听Scroll事件的HorizontalScrollView
 */
public class ObservableHorizontalScrollView extends HorizontalScrollView {

    private OnScrollListenerCompat mOnScrollListener;
    private OverScroller mScroller;

    public ObservableHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        try {
            Field scroller = HorizontalScrollView.class.getDeclaredField("mScroller");
            if (scroller != null) {
                scroller.setAccessible(true);
                mScroller = (OverScroller) scroller.get(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldL, int oldT) {
        super.onScrollChanged(l, t, oldL, oldT);
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChange(this, l, t, oldL, oldT);
        }
    }

    public void setOnScrollListener(OnScrollListenerCompat onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    public boolean canScroll() {
        return canScrollHorizontally(-1) || canScrollHorizontally(1);
    }

    public void abortScroll() {
        if (mScroller != null) {
            mScroller.abortAnimation();
        }
    }
}
