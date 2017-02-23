package com.dylan.baseui.library.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dylan.baseui.library.utils.ScreenUtils;

import java.util.List;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2015-08-08
 * Description:
 */
public class BaseApp extends Application implements Application.ActivityLifecycleCallbacks, ScreenUtils.ScreenStateListener {

    public static BaseApp mContext = null; //获取到主线程的上下文
    public static Handler mAppHandler = null; //获取到主线程的handler
    public static Looper mAppLooper = null; //获取到主线程的looper
    public static Thread mMainThread = null; //获取到主线程
    public static int mMainThreadId; //获取到主线程的id
    public static Activity currentActivity; //当前Activity实例，用于对话框需要在当前显示的Activity中弹出
    private boolean isAppInBackground = true; //应用是否在后台
    public boolean isScreenOff = false; //屏幕是否锁屏

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mAppHandler = new Handler();
        mAppLooper = getMainLooper();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();//主線程id
        //注册Activity的生命周期监听，做一些统计处理及应用前台台判断
        registerActivityLifecycleCallbacks(this);
        //监听锁屏亮屏事件
        ScreenUtils.startObserver(this, this);
    }

    public static BaseApp getApp() {
        return mContext;
    }

    public static Handler getAppHandler() {
        return mAppHandler;
    }

    public static Looper getAppLooper() {
        return mAppLooper;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取当前的进程名称
     *
     * @return
     */
    public static String getProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo info : runningApps) {
            if (info.pid == pid) {
                return info.processName;
            }
        }
        return null;
    }

    /**
     * 判断当前的线程是不是在主线程
     */
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        //每次Activity Resume时就赋值
        currentActivity = activity;
        //如果resume前是处于后台状态，那就执行resumeFromBackground操作，同时把isAppInBackground置为false
        if (isAppInBackground) {
            isAppInBackground = false;
            resumeFromBackground();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //当前Activity被销毁时（即退出了应用），把当前Activity的赋值清空
        if (activity == currentActivity) {
            currentActivity = null;
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Log.d(getClass().getSimpleName(), "app switch to background!!");
            isAppInBackground = true;
            goToBackground();
        }
    }

    /**
     * 从后台切到前台需要做的事情放这里
     */
    public void resumeFromBackground() {
        //stub
    }

    /**
     * 应用切换到后台需要做的事情放在这里
     */
    public void goToBackground() {
        //stub
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * 判断应用是否处于后台状态，有两种情况：
     * 第一种是按Home键后触发的onTrimMemory
     * 第二种是锁屏（因为锁屏是不触发onTrimMemory,所以通过监听Screen状态来处理）
     */
    public boolean isAppInBackground() {
        return isAppInBackground || isScreenOff;
    }

    @Override
    public void onScreenOn() {
        Log.d(getClass().getSimpleName(), "app screen on!!");
        isScreenOff = false;
        //如果亮屏时应用不是在后台，那直接恢复APP相关活动
        if (!isAppInBackground) {
            resumeFromBackground();
        }
    }

    @Override
    public void onScreenOff() {
        Log.d(getClass().getSimpleName(), "app screen off!!");
        isScreenOff = true;
        //如果锁屏时应用在前台，一样走进入后台的动作，暂停APP的相关活动
        if (!isAppInBackground) {
            goToBackground();
        }
    }
}
