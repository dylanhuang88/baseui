package com.dylan.baseui.library;

import android.support.annotation.ColorInt;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2016-04-18
 * Description:
 */
public interface IBaseConfig {
    boolean isRefreshEnable();
    @ColorInt
    int getRefreshViewColor();
    void resetRefreshStatus();
    void enableLoadMore(boolean enable);
    void resetLoadMoreStatus();
}
