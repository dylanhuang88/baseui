package com.dylan.baseui.library.refresh;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dylan.baseui.library.R;
import com.dylan.baseui.library.recyclerview.HeaderRecyclerViewAdapter;

import java.util.LinkedList;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2015-11-17
 * Description: 继承自谷歌官方的下拉刷新view，添加加载更多功能
 */
public class RefreshAndLoadView extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

    public static final String TAG = "RefreshAndLoadView";

    /**
     * 滑动到最下面时的上拉操作
     */
    private int mTouchSlop;
    /**
     * listView实例
     */
    private ListView mListView;
    /**
     * recyclerView实例
     */
    private RecyclerView mRecyclerView;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private IOnLoadListener mOnLoadListener;
    /**
     * 刷新监听器
     */
    private OnRefreshListener mRefreshListener;
    /**
     * ListView的加载中footer
     */
    private View listViewFooter;
    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    /**
     * 是否在加载中 (上拉加载更多)
     */
    private boolean isLoading = false;
    /**
     * 加载更多的延时，默认为1s
     */
    private long loadDelayTime = 1000;
    /**
     * 是否开启自动加载更多功能
     */
    private boolean isEnableLoading = false;
    /**
     * 是否要求SwipeRefreshLayout处理down事件，记录mActivePointerId
     */
    public boolean allowDown = true;

    private boolean isLoadMoreFooterAdded = false;

    /**
     * 在down事件的时候列表是否已经滚动到了底部
     */
    private boolean mIsBottomAtDown = false;

    /**
     * 想要设置listview的OnScrollListener方法时,需要调用setListOnScrollListener
     */
    public AbsListView.OnScrollListener listOnScrollListener = null;

    public RefreshAndLoadView(Context context) {
        super(context);
        init();
    }

    public RefreshAndLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        TextView tv = new TextView(getContext());
        tv.setText("正在加载更多...");
        int padding = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getContext().getResources().getDisplayMetrics()) + 0.5);
        tv.setPadding(0, padding, 0, padding);
        tv.setTextColor(Color.parseColor("#333333"));
        tv.setTextSize(14);
        tv.setGravity(Gravity.CENTER);
        listViewFooter = tv;

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
        if (color != 0) {
            setColorSchemeColors(color);
        }
    }

    public void setIsEnableLoading(boolean isEnableLoading) {
        this.isEnableLoading = isEnableLoading;
    }

    /**
     * 获取列表子view，ListView或者recyclerView对象
     */
    private void findListChild(View view) {
        if (view instanceof ListView) {
            mListView = (ListView) view;
            initListView();
            return;
        }

        if (view instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) view;
            initRecyclerView();
            return;
        }

        if (!(view instanceof ViewGroup)) {
            return;
        }

        ViewGroup parent = (ViewGroup) view;
        LinkedList<ViewGroup> viewSet = new LinkedList<>();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ListView) {
                mListView = (ListView) child;
                initListView();
                return;
            } else if (child instanceof RecyclerView) {
                mRecyclerView = (RecyclerView) child;
                initRecyclerView();
                return;
            } else if (child instanceof ViewGroup) {
                viewSet.addLast((ViewGroup) child);
            }
        }

        // 广度优先遍历
        while (!viewSet.isEmpty()) {
            ViewGroup next = viewSet.pollFirst();
            int count = next.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = next.getChildAt(i);
                if (child instanceof ListView) {
                    mListView = (ListView) child;
                    initListView();
                    return;
                } else if (child instanceof RecyclerView) {
                    mRecyclerView = (RecyclerView) child;
                    initRecyclerView();
                    return;
                } else if (child instanceof ViewGroup) {
                    viewSet.addLast((ViewGroup) child);
                }
            }
        }
    }

    private void initRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    //本来是判断滚动到RecyclerView的底部才添加footerView的，但是实验发现有时候滚动得快的时候虽然添加了footerView，
                    // 但是最后并没有滚动到footerView的位置并且显示出来。所以这里判断向上滚动并且没有添加过footerView的时候就马上
                    // 添加footerView。(listView没有该问题)
                    if (isEnableLoading && dy > 0 && !isLoadMoreFooterAdded) {
                        addFooterViewForRecyclerView();
                        isLoadMoreFooterAdded = true;
                    }
                    loadDataIfAtBottom();
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    //如果列表停止滚动并且已经显示出footerView而且不在加载状态则执行加载
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && isEnableLoading && !isLoading && isLoadMoreFooterAdded &&
                            isBottomForRecyclerView(mRecyclerView)) {
                        loadData();
                    }
                }
            });
        }
    }

    private void initListView() {
        if (mListView != null) {
            mListView.setOnScrollListener(this);
            // 添加一个空的footer时为了让ListView自动把adapter转换为HeaderViewListAdapter，避免低版本的一些兼容性问题
            mListView.addFooterView(new View(getContext()), null, false);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                allowDown = true;
                // 按下
                mYDown = (int) event.getRawY();
                mIsBottomAtDown = isBottomForListView(mListView) || isBottomForRecyclerView(mRecyclerView);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                // 抬起
                mLastY = (int) event.getRawY();
                loadDataIfAtBottom();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private void loadDataIfAtBottom() {
        if (isEnableLoading && canLoad()) {
            loadData();
        }
    }

    @Override
    public boolean canChildScrollUp() {
        /**
         * 每次down事件都让SwipeRefreshLayout记录mActivePointerId，避免手动调用smoothScrollToPositionFromTop时
         * 没有记录mActivePointerId导致无法下拉刷新报Got ACTION_MOVE event but don't have an active pointer id错误
         */
        if (allowDown) {
            allowDown = false;
            return false;
        } else {
            return super.canChildScrollUp();
        }
    }

    /**
     * 设置加载更多的延时
     *
     * @param loadDelayTime 加载更多的延时
     */
    public void setLoadDelayTime(long loadDelayTime) {
        this.loadDelayTime = loadDelayTime;
    }

    /**
     * 设置加载更多的View样式
     *
     * @param listViewFooter 加载更多View
     */
    public void setListViewFooter(View listViewFooter) {
        this.listViewFooter = listViewFooter;
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
     */
    private boolean canLoad() {
        return isBottom() && !isLoading && mLastY > 0 && isPullUp();
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {
        if (mListView != null) {
            return isBottomForListView(mListView);
        }

        if (mRecyclerView != null) {
            return isBottomForRecyclerView(mRecyclerView);
        }
        return false;
    }

    private boolean isBottomForListView(ListView listView) {
        if (listView != null && listView.getAdapter() != null && listView.getAdapter().getCount() > 0) {
            View view = listView.getChildAt(listView.getLastVisiblePosition());
            if (view != null) {
                //当列表项没有铺满时不显示加载更多
                if (view.getBottom() + listView.getDividerHeight() < listView.getBottom()) {
                    return false;
                }
            }
            return listView.getLastVisiblePosition() == (listView.getAdapter().getCount() - 1);
        }

        return false;
    }

    protected boolean isBottomForRecyclerView(RecyclerView recyclerView) {
        if(recyclerView == null || recyclerView.getAdapter() == null){
            return false;
        }

        //当前RecyclerView显示出来的最后一个的item的position
        int lastPosition = findLastVisibleItemPosition(recyclerView);
        View view = recyclerView.getLayoutManager().findViewByPosition(lastPosition);
        //当列表项没有铺满时不显示加载更多
        if (view != null && view.getBottom() < recyclerView.getBottom()) {
            return false;
        }

        //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
        //如果相等则说明已经滑动到最后了
        if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
            return true;
        }

        return false;
    }

    //找到数组中的最大值
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findLastVisibleItemPosition(RecyclerView recyclerView){
        //当前RecyclerView显示出来的最后一个的item的position
        int lastPosition = -1;
        if(recyclerView == null){
            return lastPosition;
        }

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            //通过LayoutManager找到当前显示的最后的item的position
            lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
            //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
            int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
            lastPosition = findMax(lastPositions);
        }

        return lastPosition;
    }

    /**
     * 是否是上拉操作
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            // 设置状态
            setLoading(true);
            // 做延时是为了能清楚看到加载状态，避免加载过快～时间可设置
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOnLoadListener.onLoadMoreData();
                }
            }, loadDelayTime);
        }
    }

    public void setLoading(boolean loading) {
        if (mListView == null && mRecyclerView == null) {
            return;
        }
        if (isLoading != loading) {
            isLoading = loading;
            if (isLoading) {
                if (!isLoadMoreFooterAdded) {
                    if (mListView != null) {
                        addFooterViewForListView();
                    }
                    if (mRecyclerView != null) {
                        addFooterViewForRecyclerView();
                    }
                    isLoadMoreFooterAdded = true;
                }

                //对已经滚动到底部的列表轻轻往上拉的时候，虽然添加了footerView,但是footerView并没有显示出来，此时要控制它滚到最底部
                if (mIsBottomAtDown) {
                    scrollListToBottom();
                }
            } else {
                if (isLoadMoreFooterAdded) {
                    if (mListView != null) {
                        removeFooterForListView();
                    }
                    if (mRecyclerView != null) {
                        removeFooterForRecyclerView();
                    }
                    isLoadMoreFooterAdded = false;
                }
                mYDown = 0;
                mLastY = 0;
            }
        }
    }

    private void scrollListToBottom() {
        post(new Runnable() {
            @Override
            public void run() {
                if(mRecyclerView != null && mRecyclerView.getAdapter() != null){
                    mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() -1);
                }
                if(mListView != null && mListView.getAdapter() != null){
                    mListView.smoothScrollToPosition(mListView.getAdapter().getCount() -1);
                }
            }
        });
    }

    private void removeFooterForRecyclerView() {
        if (mRecyclerView != null) {
            HeaderRecyclerViewAdapter adapter;
            if (mRecyclerView.getAdapter() instanceof HeaderRecyclerViewAdapter) {
                adapter = (HeaderRecyclerViewAdapter) mRecyclerView.getAdapter();
                adapter.removeFooterView(listViewFooter);
            }
        }
    }

    private void addFooterViewForRecyclerView() {
        if (mRecyclerView != null) {
            HeaderRecyclerViewAdapter adapter;
            if (mRecyclerView.getAdapter() instanceof HeaderRecyclerViewAdapter) {
                adapter = (HeaderRecyclerViewAdapter) mRecyclerView.getAdapter();
            } else {
                adapter = new HeaderRecyclerViewAdapter(mRecyclerView.getAdapter());
                mRecyclerView.setAdapter(adapter);
            }
            adapter.addFooterView(listViewFooter);
        }
    }

    private void removeFooterForListView() {
        if (mListView != null) {
            mListView.removeFooterView(listViewFooter);
        }
    }

    private void addFooterViewForListView() {
        if (mListView != null) {
            mListView.addFooterView(listViewFooter, null, false);

            if (!(mListView.getAdapter() instanceof HeaderViewListAdapter)) {
                mListView.setAdapter(mListView.getAdapter());
            }
        }
    }

    public void setOnLoadListener(IOnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (listOnScrollListener != null) {
            listOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 滚动时到了最底部也可以加载更多
        loadDataIfAtBottom();

        if (listOnScrollListener != null) {
            listOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshListener = listener;
        super.setOnRefreshListener(listener);
    }

    /**
     * 自动下拉刷新，因为调用setRefreshing(true);后,mNotify为false,不会通知mRefreshListener去调用onRefresh,所以这里手动去调用一次
     * 另外，这里把autoPullRefresh放到UI线程中操作是因为避免有些view在onCreate调用时界面布局还未完成，导致无法正确计算距离下拉动画不显示
     */
    public void autoPullRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            }
        });
    }

    public ListView getInnerListView() {
        return mListView;
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (mListView == null && mRecyclerView == null) {
            findListChild(child);
        }
    }

    /**
     * 想要设置listview的OnScrollListener方法时,需要调用此方法
     * 不要直接对listview设置
     *
     * @param onScrollListener
     */
    public void setListOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        this.listOnScrollListener = onScrollListener;
    }
}
