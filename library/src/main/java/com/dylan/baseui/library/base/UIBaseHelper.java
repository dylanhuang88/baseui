package com.dylan.baseui.library.base;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.dylan.baseui.library.IBaseConfig;
import com.dylan.baseui.library.refresh.IOnLoadListener;
import com.dylan.baseui.library.widget.stateview.IStateView;
import com.dylan.baseui.library.widget.stateview.StateRefreshView;
import com.dylan.baseui.library.widget.stateview.StateView;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2016-04-18
 * Description:
 */
public class UIBaseHelper {

    private Context context;
    private IStateView stateViewImpl;
    private IOnLoadListener onLoadImpl;
    private IBaseConfig baseConfigImpl;

    public StateRefreshView swipeRefreshView;
    public StateView stateView;

    public UIBaseHelper(Context context, IStateView view, IOnLoadListener onLoadImpl, IBaseConfig config) {
        this.context = context;
        this.onLoadImpl = onLoadImpl;
        this.stateViewImpl = view;
        this.baseConfigImpl = config;
    }

    /**
     * 初始化View，根据BaseConfig内容来决定用哪个view
     */
    public View initView(){
        stateView = new StateView(context) {
            @Override
            public View getDataView() {
                return stateViewImpl.getDataView();
            }

            @Override
            public View getEmptyView() {
                return stateViewImpl.getEmptyView();
            }

            @Override
            public View getErrorView() {
                return stateViewImpl.getErrorView();
            }

            @Override
            public View getLoadingView() {
                return stateViewImpl.getLoadingView();
            }

            @Override
            public void onLoadData() {
                stateViewImpl.onLoadData();
            }
        };
        stateView.enableErrorViewClick(true);
        if (baseConfigImpl.isRefreshEnable()) {
            swipeRefreshView = new StateRefreshView(context, stateView);
            if (baseConfigImpl.getRefreshViewColor() != -1) {
                swipeRefreshView.setColorSchemeColors(baseConfigImpl.getRefreshViewColor());
            }
            swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    stateViewImpl.onLoadData();
                }
            });
            swipeRefreshView.setOnLoadListener(onLoadImpl);
            return swipeRefreshView;
        } else {
            return stateView;
        }
    }

    /**
     * 设置StateView的状态
     */
    public void setState(IStateView.ViewState state) {
        if (swipeRefreshView != null) {
            swipeRefreshView.setRefreshing(false);
            //如果是loading状态禁用下拉刷新
            swipeRefreshView.setEnabled(state != IStateView.ViewState.LOADING);
        }
        stateView.setState(state);
    }

    /**
     * 动态设置是否启用下拉刷新功能
     */
    public void enableRefresh(boolean enable){
        if (swipeRefreshView != null) {
            swipeRefreshView.setEnabled(enable);
        }
    }


    /**
     * 设置是否启用加载更多功能
     */
    public void enableLoadMore(boolean enable){
        if (swipeRefreshView != null) {
            swipeRefreshView.setIsEnableLoading(enable);
        }
    }

    /**
     * 重置上拉加载更多状态
     */
    public void resetLoadMoreStatus(){
        if (swipeRefreshView != null) {
            swipeRefreshView.setLoading(false);
        }
    }

    /**
     * 重置理拉刷新状态
     */
    public void resetRefreshStatus() {
        if (swipeRefreshView != null) {
            swipeRefreshView.setRefreshing(false);
        }
    }

    public StateRefreshView getSwipeRefreshView() {
        return swipeRefreshView;
    }

    public StateView getStateView() {
        return stateView;
    }
}
