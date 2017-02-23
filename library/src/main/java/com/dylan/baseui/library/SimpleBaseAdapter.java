/*
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: Tony Dylan
 * Date: 2014-12-5
 * Description: adapter基类，本应用所有adapter继承此类
 * Others:
 */
package com.dylan.baseui.library;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleBaseAdapter<DATA> extends BaseAdapter {
    /**
     * 上下文实例
     */
    protected Context mContext;
    /**
     * 数据列表
     */
    public List<DATA> data;

    public SimpleBaseAdapter(Context context, List<DATA> data) {
        this.mContext = context;
        this.data = data == null ? new ArrayList() : new ArrayList(data);
    }

    public List<DATA> getData(){
        return data;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public DATA getItem(int position) {
        if (position < 0 || position >= data.size()) {
            return null;
        }
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 虚函数，继承此类的需要指定item的layout布局文件
     */
    public abstract int getItemResource();

    /**
     * 虚函数，继承此类的需要通过此函数绑定数据
     */
    public abstract View getItemView(int position, View convertView, ViewHolder holder);

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(getItemResource(), viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        return getItemView(position, view, holder);
    }

    /**
     * 添加elem中的所有数据
     *
     * @param elem list数据
     */
    public void addAll(List<DATA> elem) {
        if (elem == null) {
            elem = new ArrayList();
        }
        data.addAll(elem);
        notifyDataSetChanged();
    }

    /**
     * 删除某个数据
     *
     * @param index 指定位置
     */
    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    /**
     * 替换所有数据
     *
     * @param elem list数据
     */
    public void replaceAll(List<DATA> elem) {
        data.clear();
        if (elem != null) {
            data.addAll(elem);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    /**
     * 静态ViewHolder，优化加载数据
     */
    public class ViewHolder {
        private SparseArray<View> views = new SparseArray<View>();
        private View convertView;

        public ViewHolder(View convertView) {
            this.convertView = convertView;
        }

        public <T extends View> T getView(int resId) {
            View v = views.get(resId);
            if (null == v) {
                v = convertView.findViewById(resId);
                views.put(resId, v);
            }
            return (T) v;
        }
    }
}
