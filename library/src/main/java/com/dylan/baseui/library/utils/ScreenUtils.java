package com.dylan.baseui.library.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.PowerManager;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2016-04-28
 * Description:
 */
public class ScreenUtils {
    private static ScreenBroadcastReceiver mScreenReceiver;
    private static ScreenStateListener mScreenStateListener;

    public static void startObserver(Context context, ScreenStateListener listener) {
        mScreenStateListener = listener;
        registerListener(context);
        getScreenState(context);
    }

    public static void shutdownObserver(Context context) {
        unregisterListener(context);
    }

    /**
     * 获取screen状态
     */
    private static void getScreenState(Context context) {
        PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (manager.isScreenOn()) {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOn();
            }
        } else {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOff();
            }
        }
    }

    private static void registerListener(Context context) {
        mScreenReceiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(mScreenReceiver, filter);
    }

    private static void unregisterListener(Context context) {
        context.unregisterReceiver(mScreenReceiver);
    }


    private static class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
                if (mScreenStateListener != null) {
                    mScreenStateListener.onScreenOn();
                }
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                if (mScreenStateListener != null) {
                    mScreenStateListener.onScreenOff();
                }
            }
        }
    }

    public interface ScreenStateListener {// 返回给调用者屏幕状态信息

        void onScreenOn();

        void onScreenOff();
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

}
