package com.dylan.baseui.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2015-08-08
 * Description:
 */
public class UIUtils {

    /**
     * dip转换px
     */
    public static int dip2px(Context context, float dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics()) + 0.5);
    }

    /**
     * sp转换px
     */
    public static int sp2px(Context context, float spValue) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics()) + 0.5);
    }


    /**
     * 把自身从父View中移除
     */
    public static void removeSelfFromParent(View view) {
        // 先找到父类，再通过父类移除孩子
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    /**
     * FindViewById的泛型封装，减少强转代码
     */
    public static <T extends View> T findViewById(View layout, int id) {
        return (T) layout.findViewById(id);
    }

    /**
     * 禁止输入框弹出系统软键盘并保持光标
     *
     * @param editText 指定的输入框
     */
    public static void disableSystemSoftKeyboard(EditText editText) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 11) {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            editText.setInputType(InputType.TYPE_NULL);
        }
    }

    /**
     * 打开键盘.
     *
     * @param context the context
     */
    public static void showSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftInput(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }


    /**
     * 关闭系统的软键盘
     *
     * @param activity 指定Activity
     */
    public static void dismissSoftKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null && view.getWindowToken() != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static GradientDrawable getLineDrawable(Context context, int width, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setSize(width, dip2px(context, 1));
        drawable.setColor(color);
        drawable.setShape(GradientDrawable.RECTANGLE);
        return drawable;
    }

    /**
     * 判断根布局的可视区域与屏幕底部的差值，如果这个差大于某个值，可以认定键盘弹起了。
     * 得到的Rect就是根布局的可视区域，而rootView.bottom是其本应的底部坐标值，如果差值大于我们预设的值，就可以认定键盘弹起了。
     * 这个预设值是键盘的高度的最小值。这个rootView实际上就是DectorView，通过任意一个View再getRootView就能获得。
     */
    public static boolean isKeyboardShown(View rootView, int keyboardHeightDp) {
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > UIUtils.dip2px(rootView.getContext(), keyboardHeightDp);
    }
}
