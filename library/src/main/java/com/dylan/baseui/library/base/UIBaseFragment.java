package com.dylan.baseui.library.base;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dylan.baseui.library.IBaseConfig;
import com.dylan.baseui.library.ILayout;
import com.dylan.baseui.library.refresh.IOnLoadListener;
import com.dylan.baseui.library.widget.stateview.IStateView;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2016-03-24
 * Description:
 */
public abstract class UIBaseFragment extends Fragment implements ILayout, IStateView, IOnLoadListener, IBaseConfig {

    public boolean isInitDone = false; //是否初始化完成
    public boolean isLazyLoadTricked = false; //是否触发过懒加载
    public UIBaseHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper = new UIBaseHelper(getContext(), this, this, this);
        return helper.initView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout(helper.stateView.dataView);
        isInitDone = true;
        isLazyLoadTricked = false;
        //初始化完成后，判断fragment是否可见，满足条件就去执行加载数据动作
        if (isLazyLoadEnable() && getUserVisibleHint()){
            isLazyLoadTricked = true;
            onLoadData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        boolean change = isVisibleToUser != getUserVisibleHint();
        super.setUserVisibleHint(isVisibleToUser);
        //在viewpager中，需要使用FragmentPagerAdapter才会触发setUserVisibleHint方法
        if (change) {
            if (getUserVisibleHint()) {
                onVisible();
            } else {
                onInvisible();
            }
        }
    }

    /**
     * 可见回调
     */
    public void onVisible(){
        //如果开启了懒加载，并同时满足初始化完成以及未触发过懒加载，就去执行加载数据动作
        if (isLazyLoadEnable() && !isLazyLoadTricked && isInitDone) {
            isLazyLoadTricked = true;
            onLoadData();
        }
    }

    /**
     * 不可见回调
     */
    public void onInvisible(){
        //不可见回调
    }

    @Override
    public void setState(ViewState state) {
        helper.setState(state);
    }

    @Override
    public void onLoadMoreData() {
        //加载更多的回调
    }

    @Override
    public View getDataView() {
        return LayoutInflater.from(getContext()).inflate(getLayoutResource(), null);
    }

    /**
     * 是否启用下拉刷新
     */
    @Override
    public boolean isRefreshEnable(){
        return false;
    }

    /**
     * 是否启用懒加载模式，用于ViewPager
     */
    public boolean isLazyLoadEnable(){
        return false;
    }

    @Override
    public void enableLoadMore(boolean enable){
        helper.enableLoadMore(enable);
    }

    /**
     * 重置上拉加载更多状态
     */
    @Override
    public void resetLoadMoreStatus(){
        helper.resetLoadMoreStatus();
    }

    @Override
    public void resetRefreshStatus() {
        helper.resetRefreshStatus();
    }

    /**
     * 获取下拉刷新控件颜色
     */
    @Override
    @ColorInt
    public int getRefreshViewColor(){
        return -1;
    }


    public UIBaseHelper getHelper() {
        return helper;
    }

    /**
     * 重置懒加载标识，重置后下次可见时还会再调用加载
     */
    public void resetLazyTrickFlag() {
        setUserVisibleHint(false);
        isLazyLoadTricked = false;
    }

}
