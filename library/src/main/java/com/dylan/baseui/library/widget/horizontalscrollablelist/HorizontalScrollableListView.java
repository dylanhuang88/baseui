/*
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: angeldevil
 * Date: 16-4-11
 */
package com.dylan.baseui.library.widget.horizontalscrollablelist;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * 每一个Item中包含可滚动项的ListView，用于股票、基金列表等每一行数据过多显示不下的地方<strong>Adapter的itemView要
 * 使用{@link ObservableHorizontalScrollView}</strong>
 */
public class HorizontalScrollableListView extends ListView implements IHorizontalScrollableList, HorizontalScrollableListHelper.Callback {

    private Rect mTouchFrame = new Rect();

    private HorizontalScrollableListHelper mHelper;

    public HorizontalScrollableListView(Context context) {
        super(context);
        init();
    }

    public HorizontalScrollableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScrollableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHelper = new HorizontalScrollableListHelper(this);
    }

    @Override
    public void setHorizontalScrollPosition(int scrollX) {
        mHelper.scrollChildToPosition(this, scrollX);
    }

    @Override
    public void setOnHorizontalScrollListener(OnScrollListenerCompat onScrollListener) {
        mHelper.setOnHorizontalScrollListener(onScrollListener);
    }

    @Override
    public int getHorizontalScrollPosition() {
        return mHelper.getLastScrollX();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mHelper.dispatchTouchEvent(this, ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    protected boolean addViewInLayout(final View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        // 当用新的View时会调用这个方法
        boolean result = super.addViewInLayout(child, index, params, preventRequestLayout);

        // 先找到ObservableHorizontalScrollView，免得以下两人个API调用都要找一次
        ObservableHorizontalScrollView scrollView = mHelper.findScrollView(child);
        if (scrollView != null) {
            mHelper.fixScrollPosition(scrollView);
            fixItemClickListener(child, scrollView);
        }
        return result;
    }

    @Override
    protected void attachViewToParent(final View child, int index, ViewGroup.LayoutParams params) {
        // 当View被重用时会调用这个方法，如果没遇到问题，这个方法可以不调用fixScrollPosition，因为View是被复用的，scroll position应该是正常的
        // fixScrollPosition(child);

        // Header不属于正常的View复用
        super.attachViewToParent(child, index, params);

        // 先找到ObservableHorizontalScrollView，免得以下两人个API调用都要找一次
        ObservableHorizontalScrollView scrollView = mHelper.findScrollView(child);
        if (scrollView != null) {
//            int position = getPositionForView(scrollView);
//            if (getAdapter().getItemViewType(position) == AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER) {
                mHelper.fixScrollPosition(child);
//            }
            fixItemClickListener(child, scrollView);
        }
    }

    private void fixItemClickListener(final View child, ObservableHorizontalScrollView scrollView) {
        if (child != null && scrollView != null && scrollView.getChildCount() > 0) {
            scrollView.getChildAt(0).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListener() != null) {
                        int position = getPositionForView(child);
                        getOnItemClickListener().onItemClick(HorizontalScrollableListView.this, child, position, getItemIdAtPosition(position));
                    }
                }
            });
        }
    }

    // HorizontalScrollableListHelper.Callback start

    /**
     * See {@link ListView#pointToPosition(int, int)}，直接返回Child Index，而不是Adapter Position
     */
    @Override
    public int getPositionForPoint(int x, int y) {
        Rect frame = mTouchFrame;
        if (frame == null) {
            mTouchFrame = new Rect();
            frame = mTouchFrame;
        }

        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return i;
                }
            }
        }
        return INVALID_POSITION;
    }

    // HorizontalScrollableListHelper.Callback end
}
