/*
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: angeldevil
 * Date: 16-7-5
 */
package com.dylan.baseui.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

/**
 * 捕获ViewFlipper#onDetachedFromWindow中unregisterReceiver的异常
 */
public class SafeViewFlipper extends ViewFlipper {
    public SafeViewFlipper(Context context) {
        super(context);
    }

    public SafeViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (IllegalArgumentException e) {
            stopFlipping();
        }
    }
}
