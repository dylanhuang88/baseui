package com.dylan.baseui.library.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: adam
 * Date: 2016/6/3
 * Description:
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {

    protected Context mContext;
    protected List<T> data;
    protected OnItemClickListener<T> onItemClickListener;
    protected OnItemLongClickListener<T> onItemLongClickListener;

    public BaseRecyclerAdapter(Context context, List<T> data) {
        mContext = context;
        this.data = data == null ? new ArrayList() : new ArrayList(data);
    }

    /**
     * 虚函数，继承此类的需要指定item的layout布局文件
     */
    public abstract int getItemResource(int itemType);

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getData() {
        return data;
    }

    public T getItem(int position) {
        if (position < 0 || position >= data.size()) {
            return null;
        }
        return data.get(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(getItemResource(viewType), parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        itemView.setTag(holder);

        setListener(parent, holder);
        return holder;
    }

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int position = viewHolder.getLayoutPosition();
                    onItemClickListener.onItemClick(parent, v, data.get(position), position);
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    int position = viewHolder.getLayoutPosition();
                    return onItemLongClickListener.onItemLongClick(parent, v, data.get(position), position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.ViewHolder holder, int position) {
        render(position, holder, data.get(position));
    }

    public abstract void render(int position, ViewHolder holder, T item);

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    /**
     * 添加elem中的所有数据
     *
     * @param elem list数据
     */
    public void addAll(List<T> elem) {
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
    public void replaceAll(List<T> elem) {
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
    public class ViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> views = new SparseArray<>();

        public ViewHolder(View convertView) {
            super(convertView);
        }

        public <T extends View> T getView(int resId) {
            View v = views.get(resId);
            if (null == v) {
                v = itemView.findViewById(resId);
                views.put(resId, v);
            }
            return (T) v;
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(ViewGroup parent, View view, T t, int position);
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}

