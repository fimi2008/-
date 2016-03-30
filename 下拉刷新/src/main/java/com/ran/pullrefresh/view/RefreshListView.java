package com.ran.pullrefresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ran.pullrefresh.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新自定义控件
 * <p/>
 * 作者: wangxiang on 16/3/13 16:03
 * 邮箱: vonshine15@163.com
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private View headerView;        // 头部view
    private ImageView iv_arrow;     // 下拉箭头
    private ProgressBar pb_rotate;  // 刷新圈
    private TextView tv_state;      // 状态文本
    private TextView tv_time;       // 最后刷新时间文本

    private View footerView;        // 尾部view

    private RefreshState currentState = RefreshState.PULL; // 当前刷新状态(初始状态为下拉刷新状态)
    private RotateAnimation upAnimation, downAnimation;    // 箭头的向上旋转动画,箭头的向下旋转动画

    private int headerViewHeight;    // 头部view的高
    private int downY;               // 首次按下时的Y坐标值
    private int footerViewHeight;    // 尾部view的高

    private boolean isLoadingMore = false; // 是否加载更多

    private enum RefreshState {
        PULL("下拉刷新"), RELEASE("松开刷新"), REFRESHING("正在刷新...");
        private String tvText;

        RefreshState(String tvText) {
            this.tvText = tvText;
        }

        public String getTvText() {
            return tvText;
        }
    }


    public RefreshListView(Context context) {
        super(context);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnScrollListener(this);
        initHeaderView();
        initArrowRotateAnimation();
        initFooterView();
    }

    /**
     * 初始化尾部view
     */
    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.layout_footer, null);
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        hiddenFooterView();
        addFooterView(footerView);
    }

    /**
     * 隐藏尾部view
     */
    private void hiddenFooterView() {
        footerView.setPadding(0, -footerViewHeight, 0, 0);
    }

    /**
     * 初始化箭头的旋转动画
     */
    private void initArrowRotateAnimation() {
        upAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(300);
        upAnimation.setFillAfter(true);
        downAnimation = new RotateAnimation(-180, -360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(300);
        downAnimation.setFillAfter(true);
    }

    /**
     * 初始化headerView
     */
    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.layout_header, null);
        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pb_rotate = (ProgressBar) headerView.findViewById(R.id.pb_rotate);
        tv_state = (TextView) headerView.findViewById(R.id.tv_state);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);

        headerView.measure(0, 0); // 主动通知系统去测量该view
        headerViewHeight = headerView.getMeasuredHeight();
        hiddenHeaderView();// 隐藏headerView

        addHeaderView(headerView);
    }

    /**
     * 给下拉刷新控件绑定触摸事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 刷新状态不能进行下拉操作
                if (currentState == RefreshState.REFRESHING) {
                    break;
                }
                int deltaY = (int) (ev.getY() - downY);
                int paddingTop = deltaY - headerViewHeight;
                // 限制只有在向下滑动,并且满足adapter数据为第一条
                if (paddingTop > -headerViewHeight && getFirstVisiblePosition() == 0) {
                    headerView.setPadding(0, paddingTop, 0, 0);
                    // 从下拉刷新进入松开刷新状态
                    if (paddingTop >= 0 && currentState == RefreshState.PULL) {
                        currentState = RefreshState.RELEASE;
                    } else if (paddingTop < 0 && currentState == RefreshState.RELEASE) {
                        // 由松开刷新进入下拉刷新状态
                        currentState = RefreshState.PULL;
                    }
                    refreshHeaderView();
                    return true;    // 拦截TouchMove,不让listView处理该次move事件,会造成listView无法滑动
                }

                break;
            case MotionEvent.ACTION_UP:
                switch (currentState){
                    case PULL:
                        // 隐藏headerView
                        hiddenHeaderView();
                        break;
                    case RELEASE:
                        headerView.setPadding(0, 0, 0, 0);
                        currentState = RefreshState.REFRESHING;
                        refreshHeaderView();
                        if (null != listener){
                            listener.onPullRefresh();
                        }
                        break;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * SCROLL_STATE_IDLE:闲置状态,就是手指松开
     * SCROLL_STATE_TOUCH_SCROLL:手指触摸滑动,就是按着来滑动
     * SCROLL_STATE_FLING:快速滑动后松开
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE &&
                getLastVisiblePosition() == (getCount() - 1) && !isLoadingMore){
            isLoadingMore = true;
            footerView.setPadding(0, 0 ,0 , 0); // 显示footerView
            setSelection(getCount()); // 将对应位置的item放置到屏幕的顶端
            if (null != listener){
                listener.onLoadingMore();
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 完成刷新操作,重置状态,在你获取完数据并更新完Adapter之后,去在UI线程中调用该方法
     */
    public void completeRefresh(){
        if (isLoadingMore){
            // 重置footerView状态
            isLoadingMore = false;
            hiddenFooterView();
        }else{
            // 重置headerView状态
            hiddenHeaderView();
            currentState = RefreshState.PULL;
            pb_rotate.setVisibility(INVISIBLE);
            iv_arrow.setVisibility(VISIBLE);
            tv_state.setText(currentState.getTvText());
            tv_time.setText("最后刷新时间:"+getCurrentTime());
        }
    }

    /**
     * 获取当前时间
     * @return
     */
    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 隐藏headerView
     */
    private void hiddenHeaderView() {
        headerView.setPadding(0, -headerViewHeight, 0, 0);
    }

    /**
     * 根据currentState来更新headerView
     */
    private void refreshHeaderView() {
        tv_state.setText(currentState.getTvText());
        switch (currentState) {
            case PULL:
                iv_arrow.startAnimation(upAnimation);
                break;
            case RELEASE:
                iv_arrow.startAnimation(downAnimation);
                break;
            case REFRESHING:
                iv_arrow.clearAnimation();  // 因为向上的旋转动画有可能没有执行完
                iv_arrow.setVisibility(INVISIBLE);
                pb_rotate.setVisibility(VISIBLE);
               break;
        }
    }

    private OnRefreshListener listener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public interface OnRefreshListener{
        void onPullRefresh();

        void onLoadingMore();
    }
}