package com.dylan.baseui.library.widget.stateview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.dylan.baseui.library.widget.SafeViewFlipper;

import java.util.ArrayDeque;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2016-04-14
 * Description: 带状态（加载中、为空、错误、成功）的View
 */
public abstract class StateView extends SafeViewFlipper implements IStateView {

    public View loadingView;// 等待的view
    public View errorView;// 错误的view
    public View emptyView;// 空的view
    public View dataView;//加载后的数据view
    public ViewState state;// 默认的状态
    private View mTarget;

    public StateView(Context context) {
        super(context);
        init();
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        loadingView = getLoadingView();
        emptyView = getEmptyView();
        errorView = getErrorView();
        dataView = getDataView();
        ensureTarget();
        addView(loadingView);
        addView(emptyView);
        addView(errorView);
        addView(dataView);
        enableErrorViewClick(true);
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里设置监听是为了消费DOWN事件，才能触发MOVE事件经过SwipeRefreshLayout的onInterceptTouchEvent，才能正常看到下拉动作
                //如果不消费DOWN事件，DOWN事件会被SwipeRefreshLayout的onTouchEvent消费，
                //导致MOVE事件直接跳过onInterceptTouchEvent分发到onTouchEvent中，就看不到下拉动作*/
            }
        });
    }

    @Override
    public void setState(ViewState state) {
        //如果要设置的状态是loading或者当前状态和要设置的状态不一样时，才处理
        if (state == ViewState.LOADING || this.state != state) {
            this.state = state;
            showPagerView();
        }
    }

    /**
     * 是否开启错误view点击触发刷新功能，默认打开
     */
    public void enableErrorViewClick(boolean enable) {
        if (errorView != null) {
            errorView.setOnClickListener(enable ? new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setState(ViewState.LOADING);
                }
            } : null);
        }
    }

    private void showPagerView() {
        switch (state) {
            case LOADING:
                setDisplayedChild(0);
                onLoadData(); //开始执行加载数据操作
                break;
            case EMPTY:
                setDisplayedChild(1);
                break;
            case ERROR:
                setDisplayedChild(2);
                break;
            case SUCCESS:
                setDisplayedChild(3);
                break;
            default:
                break;
        }
    }

    /**
     * 确定用于判断canScrollVertically的View是哪一个，一般是ListView或ScrollView或webView(广度优先)
     */
    public void ensureTarget() {
        if (mTarget == null && dataView != null) {
            ArrayDeque<View> deque = new ArrayDeque<>();
            deque.add(dataView);
            while (!deque.isEmpty()) {
                View view = deque.poll();
                if (view != null) {
                    //显式指定View发须是ScrollView或者ListView或者webView（否则有可能找到一个TextView）
                    if (ViewCompat.canScrollVertically(view, 1) &&
                            (view instanceof ScrollView || view instanceof NestedScrollView || view instanceof ListView || view instanceof WebView
                                    || view instanceof RecyclerView)) {
                        mTarget = view;
                        break;
                    } else if (view instanceof ViewGroup) {
                        ViewGroup group = (ViewGroup) view;
                        for (int i = 0; i < group.getChildCount(); i++) {
                            View child = group.getChildAt(i);
                            deque.add(child);
                        }
                    }
                }
            }
        }
    }


    /**
     * 由于这是自定义View，用在SwipeRefreshLayout里需要重写canScrollVertically来判断是否可向上滚动
     */
    @Override
    public boolean canScrollVertically(int direction) {
        if (mTarget != null) {
            return ViewCompat.canScrollVertically(mTarget, direction);
        }
        return super.canScrollVertically(direction);
    }
}
