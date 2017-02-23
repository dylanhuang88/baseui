package com.dylan.baseui.library.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: adam
 * Date: 2016/12/6
 * Description:
 */
public class RecyclerViewAdapterWrapper extends RecyclerView.Adapter {

    protected final RecyclerView.Adapter wrappedAdapter;

    public RecyclerViewAdapterWrapper(final RecyclerView.Adapter wrappedAdapter) {
        super();
        this.wrappedAdapter = wrappedAdapter;

        super.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (wrappedAdapter != null) {
                    wrappedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                if (wrappedAdapter != null) {
                    wrappedAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
                }
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                if (wrappedAdapter != null) {
                    wrappedAdapter.notifyItemRangeChanged(positionStart, itemCount);
                }
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (wrappedAdapter != null) {
                    wrappedAdapter.notifyItemRangeChanged(positionStart, itemCount);
                }
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                if (wrappedAdapter != null) {
                    wrappedAdapter.notifyItemRangeRemoved(positionStart, itemCount);
                }
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                if (wrappedAdapter != null) {
                    getWrappedAdapter().notifyItemMoved(fromPosition, toPosition);
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (wrappedAdapter != null) {
            return wrappedAdapter.onCreateViewHolder(parent, viewType);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (wrappedAdapter != null) {
            wrappedAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (wrappedAdapter != null) {
            return wrappedAdapter.getItemCount();
        }

        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (wrappedAdapter != null) {
            return wrappedAdapter.getItemViewType(position);
        }

        return super.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        if (wrappedAdapter != null) {
            wrappedAdapter.setHasStableIds(hasStableIds);
        }
    }

    @Override
    public long getItemId(int position) {
        if (wrappedAdapter != null) {
            return wrappedAdapter.getItemId(position);
        }

        return super.getItemId(position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (wrappedAdapter != null) {
            wrappedAdapter.onViewRecycled(holder);
        }
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        if (wrappedAdapter != null) {
            return wrappedAdapter.onFailedToRecycleView(holder);
        }

        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (wrappedAdapter != null) {
            wrappedAdapter.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (wrappedAdapter != null) {
            wrappedAdapter.onViewDetachedFromWindow(holder);
        }
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (wrappedAdapter != null) {
            wrappedAdapter.registerAdapterDataObserver(observer);
        }
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (wrappedAdapter != null) {
            wrappedAdapter.unregisterAdapterDataObserver(observer);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (wrappedAdapter != null) {
            wrappedAdapter.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (wrappedAdapter != null) {
            wrappedAdapter.onDetachedFromRecyclerView(recyclerView);
        }
    }

    public RecyclerView.Adapter getWrappedAdapter() {
        return wrappedAdapter;
    }


}

