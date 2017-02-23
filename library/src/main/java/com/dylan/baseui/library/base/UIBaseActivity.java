package com.dylan.baseui.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.dylan.baseui.library.IBaseConfig;
import com.dylan.baseui.library.ILayout;
import com.dylan.baseui.library.refresh.IOnLoadListener;
import com.dylan.baseui.library.widget.stateview.IStateView;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2016-04-14
 * Description:
 */
public abstract class UIBaseActivity extends AppCompatActivity implements ILayout, IStateView, IOnLoadListener, IBaseConfig {

    public UIBaseHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new UIBaseHelper(this, this, this, this);
        setContentView(helper.initView());
        preInit(savedInstanceState); //做一些比View更基础的初始化动作，例如ActionBar
        if (!interceptInitLayout()) {
            initLayout(helper.stateView.dataView);
        }
    }

    public boolean interceptInitLayout() {
        return false;
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
        return LayoutInflater.from(this).inflate(getLayoutResource(), null);
    }

    /**
     * 是否启用下拉刷新
     */
    @Override
    public boolean isRefreshEnable(){
        return false;
    }

    /**
     * 设置是否启用加载更多功能
     */
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
    public int getRefreshViewColor(){
        return -1;
    }

    /**
     * 提供给子类覆写做一些比View更基础的初始化动作，例如ActionBar
     */
    public void preInit(@Nullable Bundle savedInstanceState) {
        //for overwrite!
    }


    public UIBaseHelper getHelper() {
        return helper;
    }
}
