package com.dylan.baseui.library.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2017-02-23
 * Description:
 */

public class PermissionHelper implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "PermissionHelper";
    private int mCurrentPermissionRequestCode = -1;
    private EasyPermissions.PermissionCallbacks mCallbacks;

    public PermissionHelper(EasyPermissions.PermissionCallbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    /**
     * 动态请求权限，这里要先记住请求的code，便于后面处理区分
     *
     * @param rationale   当用户拒绝过时，说明需要权限的原因
     * @param requestCode 请求code
     * @param perms       请求的权限
     */
    public void requestActivityPermissions(Activity activity, String rationale, int requestCode, @NonNull final String... perms) {
        //记住requestCode，如果不记住在处理时就不知道这个是不是对应的请求，会导致Activity和Fragment重复处理的情况
        mCurrentPermissionRequestCode = requestCode;
        EasyPermissions.requestPermissions(activity, rationale, requestCode, perms);
    }

    public void requestFragmentPermissions(Fragment fragment, String rationale, int requestCode, @NonNull final String... perms) {
        //记住requestCode，如果不记住在处理时就不知道这个是不是对应的请求，会导致Activity和Fragment重复处理的情况
        mCurrentPermissionRequestCode = requestCode;
        EasyPermissions.requestPermissions(fragment, rationale, requestCode, perms);
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == mCurrentPermissionRequestCode) {
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == mCurrentPermissionRequestCode && mCallbacks != null) {
            mCallbacks.onPermissionsGranted(requestCode, perms);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == mCurrentPermissionRequestCode && mCallbacks != null) {
            mCallbacks.onPermissionsDenied(requestCode, perms);
        }
    }
}
