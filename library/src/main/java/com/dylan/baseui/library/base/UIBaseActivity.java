package com.dylan.baseui.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.dylan.baseui.library.IBaseConfig;
import com.dylan.baseui.library.ILayout;
import com.dylan.baseui.library.R;
import com.dylan.baseui.library.refresh.IOnLoadListener;
import com.dylan.baseui.library.utils.PermissionHelper;
import com.dylan.baseui.library.widget.stateview.IStateView;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2016-04-14
 * Description:
 */
public abstract class UIBaseActivity extends AppCompatActivity implements ILayout, IStateView, IOnLoadListener,
        IBaseConfig, EasyPermissions.PermissionCallbacks {

    public UIBaseHelper helper;
    private int mCurrentPermissionRequestCode = -1;
    private PermissionHelper mPermissionHelper = new PermissionHelper(this);

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
    public boolean isRefreshEnable() {
        return false;
    }

    /**
     * 设置是否启用加载更多功能
     */
    @Override
    public void enableLoadMore(boolean enable) {
        helper.enableLoadMore(enable);
    }

    /**
     * 重置上拉加载更多状态
     */
    @Override
    public void resetLoadMoreStatus() {
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
    public int getRefreshViewColor() {
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

    public PermissionHelper getPermissionHelper() {
        return mPermissionHelper;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // activity可覆写进行获取权限后的处理
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // activity可覆写进行获取权限失败的处理
        // 如果用户勾选了“不再询问”，说明情况后提示可以去应用设置界面再打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.base_ui_rationale_ask_again))
                    .setTitle(getString(R.string.base_ui_title_settings_dialog))
                    .setPositiveButton(getString(R.string.base_ui_setting))
                    .setNegativeButton(getString(R.string.base_ui_cancel), null)
                    .build()
                    .show();
        }
    }
}
