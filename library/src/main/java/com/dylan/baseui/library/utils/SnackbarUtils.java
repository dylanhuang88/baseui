package com.dylan.baseui.library.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dylan.baseui.library.R;

import java.lang.ref.WeakReference;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2017-01-07
 * Description: Snackbar工具类，可以链式调用，方便的设置背景色及文字颜色，并有四种状态可以使用
 */

public class SnackbarUtils {
    private static final String TAG = "SnackbarUtils";
    //默认Snackbar背景颜色
    private static final int COLOR_INFO = 0XFF2094F3;
    private static final int COLOR_CONFIRM = 0XFF4CB04E;
    private static final int COLOR_WARNING = 0XFFFEC005;
    private static final int COLOR_Error = 0XFFF44336;
    private static final int COLOR_MESSAGE = 0XFFFFFFFF;
    //四种背景色
    private static int sBgColorInfo = COLOR_INFO;
    private static int sBgColorConfirm = COLOR_CONFIRM;
    private static int sBgColorWarning = COLOR_WARNING;
    private static int sBgColorError = COLOR_Error;
    //四种文字色
    private static int sMsgColorInfo = COLOR_MESSAGE;
    private static int sMsgColorConfirm = COLOR_MESSAGE;
    private static int sMsgColorWarning = COLOR_MESSAGE;
    private static int sMsgColorError = COLOR_MESSAGE;

    //工具类当前持有的Snackbar实例
    private WeakReference<Snackbar> snackbarWeakReference;

    /**
     * 设置四种状态的背景色
     */
    public static void initSanckbarBackgroundColor(@ColorInt int info, @ColorInt int confirm, @ColorInt int warning, @ColorInt int error) {
        sBgColorInfo = info;
        sBgColorConfirm = confirm;
        sBgColorWarning = warning;
        sBgColorError = error;
    }

    /**
     * 设置四种状态的文字色
     */
    public static void initSanckbarMsgColor(@ColorInt int info, @ColorInt int confirm, @ColorInt int warning, @ColorInt int error) {
        sMsgColorInfo = info;
        sMsgColorConfirm = confirm;
        sMsgColorWarning = warning;
        sMsgColorError = error;
    }

    private SnackbarUtils() {
        throw new RuntimeException("禁止无参创建实例");
    }

    private SnackbarUtils(@Nullable WeakReference<Snackbar> reference) {
        snackbarWeakReference = reference;
    }

    /**
     * 获取 Snackbar
     */
    public Snackbar getSnackbar() {
        if (snackbarWeakReference != null && snackbarWeakReference.get() != null) {
            return snackbarWeakReference.get();
        } else {
            return null;
        }
    }

    /**
     * 初始化Snackbar实例
     * 展示时间:Snackbar.LENGTH_SHORT
     */
    public static SnackbarUtils makeShort(View view, String message) {
        return new SnackbarUtils(new WeakReference<>(Snackbar.make(view, message, Snackbar.LENGTH_SHORT)));
    }

    /**
     * 初始化Snackbar实例
     * 展示时间:Snackbar.LENGTH_LONG
     */
    public static SnackbarUtils makeLong(View view, String message) {
        return new SnackbarUtils(new WeakReference<>(Snackbar.make(view, message, Snackbar.LENGTH_LONG)));
    }

    /**
     * 初始化Snackbar实例
     * 展示时间:Snackbar.LENGTH_INDEFINITE
     */
    public static SnackbarUtils makeIndefinite(View view, String message) {
        return new SnackbarUtils(new WeakReference<>(Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)));
    }

    /**
     * 初始化Snackbar实例
     * 展示时间:duration 毫秒
     */
    public static SnackbarUtils makeCustom(View view, String message, int duration) {
        return new SnackbarUtils(new WeakReference<>(Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setDuration(duration)));
    }

    /**
     * 设置mSnackbar背景色为提醒色
     */
    public SnackbarUtils info() {
        if (getSnackbar() != null) {
            getSnackbar().getView().setBackgroundColor(sBgColorInfo);
        }
        return this.messageColor(sMsgColorInfo);
    }

    /**
     * 设置Snackbar背景色为确认色
     */
    public SnackbarUtils confirm() {
        if (getSnackbar() != null) {
            getSnackbar().getView().setBackgroundColor(sBgColorConfirm);
        }
        return this.messageColor(sMsgColorConfirm);
    }

    /**
     * 设置Snackbar背景色为警告色
     */
    public SnackbarUtils warning() {
        if (getSnackbar() != null) {
            getSnackbar().getView().setBackgroundColor(sBgColorWarning);
        }
        return this.messageColor(sMsgColorWarning);
    }

    /**
     * 设置Snackbar背景色为错误色
     */
    public SnackbarUtils error() {
        if (getSnackbar() != null) {
            getSnackbar().getView().setBackgroundColor(sBgColorError);
        }
        return this.messageColor(sMsgColorError);
    }

    /**
     * 设置Snackbar背景色
     */
    public SnackbarUtils backColor(@ColorInt int backgroundColor) {
        if (getSnackbar() != null) {
            getSnackbar().getView().setBackgroundColor(backgroundColor);
        }
        return this;
    }

    /**
     * 设置TextView(@+id/snackbar_text)的文字颜色
     */
    public SnackbarUtils messageColor(@ColorInt int messageColor) {
        if (getSnackbar() != null) {
            ((TextView) getSnackbar().getView().findViewById(R.id.snackbar_text)).setTextColor(messageColor);
        }
        return this;
    }

    /**
     * 设置Button(@+id/snackbar_action)的文字颜色
     */
    public SnackbarUtils actionColor(@ColorInt int actionTextColor) {
        if (getSnackbar() != null) {
            ((Button) getSnackbar().getView().findViewById(R.id.snackbar_action)).setTextColor(actionTextColor);
        }
        return this;
    }

    /**
     * 设置Snackbar背景色 + TextView(@+id/snackbar_text)的文字颜色 + Button(@+id/snackbar_action)的文字颜色
     */
    public SnackbarUtils colors(@ColorInt int backgroundColor, @ColorInt int messageColor, @ColorInt int actionTextColor) {
        if (getSnackbar() != null) {
            getSnackbar().getView().setBackgroundColor(backgroundColor);
            ((TextView) getSnackbar().getView().findViewById(R.id.snackbar_text)).setTextColor(messageColor);
            ((Button) getSnackbar().getView().findViewById(R.id.snackbar_action)).setTextColor(actionTextColor);
        }
        return this;
    }

    /**
     * 设置Snackbar 背景透明度
     */
    public SnackbarUtils alpha(float alpha) {
        if (getSnackbar() != null) {
            alpha = alpha >= 1.0f ? 1.0f : (alpha <= 0.0f ? 0.0f : alpha);
            getSnackbar().getView().setAlpha(alpha);
        }
        return this;
    }

    /**
     * 设置按钮文字内容 及 点击监听
     * {@link Snackbar#setAction(CharSequence, View.OnClickListener)}
     */
    public SnackbarUtils setAction(@StringRes int resId, View.OnClickListener listener) {
        if (getSnackbar() != null) {
            return setAction(getSnackbar().getView().getResources().getText(resId), listener);
        } else {
            return this;
        }
    }

    /**
     * 设置按钮文字内容 及 点击监听
     * {@link Snackbar#setAction(CharSequence, View.OnClickListener)}
     */
    public SnackbarUtils setAction(CharSequence text, View.OnClickListener listener) {
        if (getSnackbar() != null) {
            getSnackbar().setAction(text, listener);
        }
        return this;
    }

    /**
     * 设置 mSnackbar 展示完成 及 隐藏完成 的监听
     */
    public SnackbarUtils setCallback(Snackbar.Callback setCallback) {
        if (getSnackbar() != null) {
            getSnackbar().setCallback(setCallback);
        }
        return this;
    }

    /**
     * 设置TextView(@+id/snackbar_text)中文字的对齐方式 居中
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public SnackbarUtils messageCenter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getSnackbar() != null) {
                TextView message = (TextView) getSnackbar().getView().findViewById(R.id.snackbar_text);
                //View.setTextAlignment需要SDK>=17
                message.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                message.setGravity(Gravity.CENTER);
            }
        }
        return this;
    }

    /**
     * 设置TextView(@+id/snackbar_text)中文字的对齐方式 居右
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public SnackbarUtils messageRight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getSnackbar() != null) {
                TextView message = (TextView) getSnackbar().getView().findViewById(R.id.snackbar_text);
                //View.setTextAlignment需要SDK>=17
                message.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                message.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            }
        }
        return this;
    }

    /**
     * 向Snackbar布局中添加View(Google不建议,复杂的布局应该使用DialogFragment进行展示)
     */
    public SnackbarUtils addView(int layoutId, int index) {
        if (getSnackbar() != null) {
            //加载布局文件新建View
            View addView = LayoutInflater.from(getSnackbar().getView().getContext()).inflate(layoutId, null);
            return addView(addView, index);
        } else {
            return this;
        }
    }

    /**
     * 向Snackbar布局中添加View(Google不建议,复杂的布局应该使用DialogFragment进行展示)
     */
    public SnackbarUtils addView(View addView, int index) {
        if (getSnackbar() != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);//设置新建布局参数
            //设置新建View在Snackbar内垂直居中显示
            params.gravity = Gravity.CENTER_VERTICAL;
            addView.setLayoutParams(params);
            ((Snackbar.SnackbarLayout) getSnackbar().getView()).addView(addView, index);
        }
        return this;
    }

    /**
     * 设置Snackbar布局的外边距
     * 注:经试验发现,调用margins后再调用 gravityFrameLayout,则margins无效.
     * 为保证margins有效,应该先调用 gravityFrameLayout,在 show() 之前调用 margins
     */
    public SnackbarUtils margins(int margin) {
        if (getSnackbar() != null) {
            return margins(margin, margin, margin, margin);
        } else {
            return this;
        }
    }

    /**
     * 设置Snackbar布局的外边距
     * 注:经试验发现,调用margins后再调用 gravityFrameLayout,则margins无效.
     * 为保证margins有效,应该先调用 gravityFrameLayout,在 show() 之前调用 margins
     */
    public SnackbarUtils margins(int left, int top, int right, int bottom) {
        if (getSnackbar() != null) {
            ViewGroup.LayoutParams params = getSnackbar().getView().getLayoutParams();
            ((ViewGroup.MarginLayoutParams) params).setMargins(left, top, right, bottom);
            getSnackbar().getView().setLayoutParams(params);
        }
        return this;
    }

    /**
     * 显示 Snackbar
     */
    public void show() {
        if (getSnackbar() != null) {
            getSnackbar().show();
        } else {
            Log.e(TAG, "已经被回收");
        }
    }
}