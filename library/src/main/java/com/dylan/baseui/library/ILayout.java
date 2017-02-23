package com.dylan.baseui.library;

import android.view.View;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2016-03-24
 * Description:
 */
public interface ILayout {
    //虚函数，继承此类指定Activity的layout布局文件
    int getLayoutResource();
    //虚函数，继承此类初始化个性化的布局
    void initLayout(View view);
}
