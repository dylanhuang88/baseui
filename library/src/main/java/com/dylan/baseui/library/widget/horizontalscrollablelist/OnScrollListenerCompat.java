/*
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: angeldevil
 * Date: 16-4-11
 */
package com.dylan.baseui.library.widget.horizontalscrollablelist;

import android.view.View;

/**
 * Copy of {@link View.OnScrollChangeListener} which is added in API 23
 */
public interface OnScrollListenerCompat {
    /**
     * Called when the scroll position of a view changes.
     *
     * @param v          The view whose scroll position has changed.
     * @param scrollX    Current horizontal scroll origin.
     * @param scrollY    Current vertical scroll origin.
     * @param oldScrollX Previous horizontal scroll origin.
     * @param oldScrollY Previous vertical scroll origin.
     */
    void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
}
