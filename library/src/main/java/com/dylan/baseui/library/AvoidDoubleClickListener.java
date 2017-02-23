package com.dylan.baseui.library;

import android.view.View;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2015-05-29
 * Description: 避免频繁点击的监听器
 */
public abstract class AvoidDoubleClickListener implements View.OnClickListener {

    /**
     * 两次点击的最小间隔的默认值为500ms
     */
    public static final int MIN_CLICK_DELAY_TIME = 500;
    /**
     * 上次点击的时间
     */
    private long lastClickTime = 0;
    /**
     * 定义两次点击的最小间隔
     */
    private int minTimeInterval = MIN_CLICK_DELAY_TIME;

    /**
     * 点击事件的回调
     */
    public abstract void onAvoidDoubleClick(View view);

    public AvoidDoubleClickListener() {
    }

    public AvoidDoubleClickListener(int minTimeInterval) {
        this.minTimeInterval = minTimeInterval;
    }

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > minTimeInterval) {
            onAvoidDoubleClick(v);
        }
        //无论有没回调都记录点击的时间，这样可以有效避免连续点击
        lastClickTime = currentTime;
    }
}
