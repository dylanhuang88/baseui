package com.dylan.baseui.library.widget.stateview;

import android.view.View;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2016-04-14
 * Description:
 */
public interface IStateView {
    enum ViewState {
        LOADING, ERROR, EMPTY, SUCCESS
    }
    View getDataView(); //获取数据视图
    View getEmptyView(); //获取为空视图
    View getErrorView(); //获取错误视图
    View getLoadingView(); //获取加载中视图
    void setState(ViewState state); //设置状态
    void onLoadData(); //加载数据
}
