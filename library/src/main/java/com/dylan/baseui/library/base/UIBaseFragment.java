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
 * Date: 2016-03-24
 * Description:
 */
public abstract class UIBaseFragment extends Fragment implements ILayout, IStateView, IOnLoadListener,
        IBaseConfig, EasyPermissions.PermissionCallbacks {

    public boolean isInitDone = false; //是否初始化完成
    public boolean isLazyLoadTricked = false; //是否触发过懒加载
    public UIBaseHelper helper;
    private PermissionHelper mPermissionHelper = new PermissionHelper(this);

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
        if (isLazyLoadEnable() && getUserVisibleHint()) {
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
    public void onVisible() {
        //如果开启了懒加载，并同时满足初始化完成以及未触发过懒加载，就去执行加载数据动作
        if (isLazyLoadEnable() && !isLazyLoadTricked && isInitDone) {
            isLazyLoadTricked = true;
            onLoadData();
        }
    }

    /**
     * 不可见回调
     */
    public void onInvisible() {
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
    public boolean isRefreshEnable() {
        return false;
    }

    /**
     * 是否启用懒加载模式，用于ViewPager
     */
    public boolean isLazyLoadEnable() {
        return false;
    }

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
    @ColorInt
    public int getRefreshViewColor() {
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
