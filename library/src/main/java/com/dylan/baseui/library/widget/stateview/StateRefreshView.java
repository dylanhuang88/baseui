package com.dylan.baseui.library.widget.stateview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dylan.baseui.library.refresh.RefreshAndLoadView;


/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2015-10-28
 * Description: 带下拉刷新的状态View
 */
public class StateRefreshView extends RefreshAndLoadView {

    public StateView stateView; //包含状态的View

    public StateRefreshView(Context context, StateView view) {
        super(context);
        init(view);
    }

    public StateRefreshView(Context context, AttributeSet attrs, StateView view) {
        super(context, attrs);
        init(view);
    }

    private void init(StateView view){
        if (view == null){
            throw new RuntimeException("StateView 实例不允许为空");
        }
        stateView = view;
        addView(view);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //有事件分发时去找到stateView里可向下滚动的view（因为如果是listView的话，需要填充了数据后才可向下滚动，所以找了用户操作触发点去判断目标）
        if (ev.getAction() == MotionEvent.ACTION_DOWN && stateView != null){
            stateView.ensureTarget();
        }
        return super.dispatchTouchEvent(ev);
    }

}
