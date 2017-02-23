/*
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: angeldevil
 * Date: 16-4-13
 */
package com.dylan.baseui.library.widget.horizontalscrollablelist;

/**
 * 每一个Item中包含可滚动项的列表，用于股票、基金列表等每一行数据过多显示不下的地方
 */
public interface IHorizontalScrollableList {

    void setHorizontalScrollPosition(int scrollX);

    void setOnHorizontalScrollListener(OnScrollListenerCompat onScrollListener);

    int getHorizontalScrollPosition();
}
