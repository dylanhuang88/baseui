package com.dylan.baseui.library.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: adam
 * Date: 2016/12/6
 * Description:
 */
public class HeaderRecyclerViewAdapter extends RecyclerViewAdapterWrapper {

    private RecyclerView.LayoutManager mLayoutManager;

    private List<View> mHeaderViews = new ArrayList<>();
    private List<View> mFooterViews = new ArrayList<>();

    private final static int MAX_HEADER_COUNT = 100;
    private final static int MAX_FOOTER_COUNT = MAX_HEADER_COUNT;

    //定义FooterView类型 和 HeaderView类型
    private final static int HEADER_VIEW_TYPE_BASE = 1083;
    private final static int FOOTER_VIEW_TYPE_BASE = HEADER_VIEW_TYPE_BASE + MAX_HEADER_COUNT;

    public HeaderRecyclerViewAdapter(@NonNull RecyclerView.Adapter targetAdapter) {
        super(targetAdapter);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mLayoutManager = recyclerView.getLayoutManager();
        setGridHeaderFooter(mLayoutManager);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + mHeaderViews.size() + mFooterViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = mHeaderViews.size();
        if (numHeaders > position) {
            return HEADER_VIEW_TYPE_BASE + position;
        }

        int adapterCount = super.getItemCount();
        int adjPosition = position - numHeaders;
        if (adjPosition < adapterCount) {
            return super.getItemViewType(adjPosition);
        }

        return FOOTER_VIEW_TYPE_BASE + (adjPosition - adapterCount);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        if (isHeaderViewType(viewType)) {
            itemView = mHeaderViews.get(viewType - HEADER_VIEW_TYPE_BASE);
        } else if (isFooterViewType(viewType)) {
            itemView = mFooterViews.get(viewType - FOOTER_VIEW_TYPE_BASE);
        }

        if (itemView != null) {
            //set StaggeredGridLayoutManager header & footer view
            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                ViewGroup.LayoutParams targetParams = itemView.getLayoutParams();
                StaggeredGridLayoutManager.LayoutParams StaggerLayoutParams;
                if (targetParams != null) {
                    StaggerLayoutParams = new StaggeredGridLayoutManager.LayoutParams(targetParams.width, targetParams.height);
                } else {
                    StaggerLayoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                            .WRAP_CONTENT);
                }
                StaggerLayoutParams.setFullSpan(true);
                itemView.setLayoutParams(StaggerLayoutParams);
            }
            return new RecyclerView.ViewHolder(itemView) {
            };
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int headerNum = mHeaderViews.size();
        if (position >= headerNum) {
            int adCount = super.getItemCount();
            int adjPosition = position - headerNum;
            if (adjPosition < adCount) {
                super.onBindViewHolder(holder, adjPosition);
            }
        }
    }

    private boolean isHeaderViewType(int type) {
        return type >= HEADER_VIEW_TYPE_BASE && type < HEADER_VIEW_TYPE_BASE + MAX_HEADER_COUNT;
    }

    private boolean isFooterViewType(int type) {
        return type >= FOOTER_VIEW_TYPE_BASE && type < FOOTER_VIEW_TYPE_BASE + MAX_FOOTER_COUNT;
    }

    private void setGridHeaderFooter(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHeaderView(position) || isFooterView(position)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFooterViews.size();
    }

    public void removeHeaderView(View view) {
        if (mHeaderViews.contains(view)) {
            mHeaderViews.remove(view);
        }
        notifyDataSetChanged();
    }

    public void removeFooterView(View view) {
        if (mFooterViews.contains(view)) {
            mFooterViews.remove(view);
        }
        notifyDataSetChanged();
    }

    public void addHeaderView(View view) {
        if (!mHeaderViews.contains(view)) {
            mHeaderViews.add(view);
        }
        notifyDataSetChanged();
    }

    public void addFooterView(View view) {
        if (!mFooterViews.contains(view)) {
            mFooterViews.add(view);
        }
        notifyDataSetChanged();
    }

    private boolean isHeaderView(int position) {
        return position < mHeaderViews.size();
    }

    private boolean isFooterView(int position) {
        int headerSize = mHeaderViews.size();
        int footerSize = mFooterViews.size();
        int adSize = super.getItemCount();
        return position >= headerSize + adSize && position < headerSize + footerSize + adSize;
    }
}
