/*
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: angeldevil
 * Date: 16-4-13
 */
package com.dylan.baseui.library.widget.horizontalscrollablelist;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class HorizontalScrollableListHelper {

    /**
     * 上一次水平滚动停留的位置，ListView的LastVisiblePosition + 1位置的View可能本来就已经生成了，但不可见，所以遍历ListView的Child时遍历不到，
     * ListView滑动时这个View也不是被重用的View（因为本来就在），会导致ObservableHorizontalScrollView的位置不同步
     */
    private int mLastScrollX = -1;
    private int mLastScrollY = -1;

    private ObservableHorizontalScrollView mLastTouchedScrollView;

    private OnScrollListenerCompat mOnScrollListener;

    private Callback mCallback;

    public HorizontalScrollableListHelper(Callback callback) {
        this.mCallback = callback;
    }

    /**
     * 会拦截多手指的Event
     * @return true View应该直接return true, false View可以继续做自己的处理
     */
    public boolean dispatchTouchEvent(final ViewGroup view, MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_POINTER_DOWN || action == MotionEvent.ACTION_POINTER_UP) {
            return true;
        }
        if (action != MotionEvent.ACTION_DOWN) {
            return false;
        }
        int childCount = view.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = view.getChildAt(i);
            ObservableHorizontalScrollView scrollView = findScrollView(child);
            if (scrollView != null) {
                scrollView.abortScroll();
            }
        }
        if (mLastTouchedScrollView != null) {
            mLastTouchedScrollView.setOnScrollListener(null);
            mLastTouchedScrollView = null;
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int position = getPositionForPoint(x, y);
        if (position < 0 && position >= view.getChildCount()) {
            return false;
        }
        View child = view.getChildAt(position);
        final ObservableHorizontalScrollView scrollView = findScrollView(child);
        if (scrollView == null) {
            return false;
        }
        mLastTouchedScrollView = scrollView;
        scrollView.setOnScrollListener(new OnScrollListenerCompat() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollChildToPosition(view, scrollX, scrollY);
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrollChange(v, scrollX, scrollY, oldScrollX, oldScrollY);
                }
            }
        });
        return false;
    }


    public void setOnHorizontalScrollListener(OnScrollListenerCompat onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    /**
     * 在<code>view</code>的Child View中找<code>ObservableHorizontalScrollView</code>, 深度优先
     *
     * @return 找到的ObservableHorizontalScrollView或null
     */
    public ObservableHorizontalScrollView findScrollView(View view) {
        if (view instanceof ObservableHorizontalScrollView) {
            return (ObservableHorizontalScrollView) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                View v = parent.getChildAt(i);
                v = findScrollView(v);
                if (v != null) {
                    return (ObservableHorizontalScrollView) v;
                }
            }
        }
        return null;
    }

    public int getLastScrollX() {
        return mLastScrollX;
    }

    public void scrollChildToPosition(ViewGroup view, int scrollX) {
        scrollChildToPosition(view, scrollX, mLastScrollY);
    }

    public void scrollChildToPosition(ViewGroup view, int scrollX, int scrollY) {
        if (mLastScrollX != scrollX) {
            if (scrollY < 0) {
                scrollY = 0;
            }
            mLastScrollX = scrollX;
            mLastScrollY = scrollY;

            for (int i = 0; i < view.getChildCount(); i++) {
                HorizontalScrollView sView = findScrollView(view.getChildAt(i));
                if (sView != null && sView.getScrollX() != scrollX) {
                    sView.scrollTo(scrollX, scrollY);
                }
            }
        }
    }

    /**
     * 修正ObservableHorizontalScrollView的scrollX， See {@link HorizontalScrollableListHelper#mLastScrollX}
     */
    public void fixScrollPosition(View child) {
        final int lastScrollX = getLastScrollX();
        if (lastScrollX != -1) {
            final ObservableHorizontalScrollView scrollView = findScrollView(child);
            if (scrollView != null && scrollView.getScrollX() != lastScrollX) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(lastScrollX, scrollView.getScrollY());
                    }
                });
            }
        }
    }

    /**
     * 获取点击位置的Child View Position
     *
     * @return Child View的Position或－1
     */
    private int getPositionForPoint(int x, int y) {
        int position = -1;
        if (mCallback != null) {
            position = mCallback.getPositionForPoint(x, y);
        }
        return position;
    }

    public interface Callback {
        /**
         * 获取点击位置的Child View Position
         *
         * @return Child View的Position或－1
         */
        int getPositionForPoint(int x, int y);
    }
}
