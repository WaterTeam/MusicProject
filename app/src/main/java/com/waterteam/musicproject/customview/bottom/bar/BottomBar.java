package com.waterteam.musicproject.customview.bottom.bar;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.Toast;


import com.waterteam.musicproject.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 基本功能的BottomBar
 * Created by Administrator on 2018/3/13.
 */

public abstract class BottomBar extends FrameLayout {
    private static final String TAG = "BottomBar";

    //头部
    public View bottomBarHead;
    //尾部
    public View bottomBarContent;

    //处理滚动动画的类
    public Scroller defaultScroller;

    //判断手指按下的位置
    private int downX, downY, startX, startY;

    //底部已经滑动的距离
    private int scrollOffset;

    //判断是否为播放界面
    public boolean isPullUp = false;

    //触摸监听
    private BottomBarHandle touchHandle;

    //动画监听
    private OnScrollerListener scrollerListener;

    //上下拉状态改变监听
    private BottomBar.OnUpOrDownListener onUpOrDownListener;

    public BottomBar(@NonNull Context context) {
        super(context);
    }

    public BottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 控制滑动动画的值变化，如需要可复写
     *
     * @param
     * @return
     * @throws
     * @author Administrator on 2018/3/13.
     */
    public void initScroller() {
          /*
      控制栏的可视范围
     */
        defaultScroller = new Scroller(getContext());
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initScroller();
        initView();

    }

    public void initView() {
        bottomBarHead = getChildAt(0);
        bottomBarContent = getChildAt(1);
        Log.e(TAG, "initView: oioi" +bottomBarContent);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: 0");
                startX = (int) event.getX();
                startY = (int) event.getY();
                downX = (int) event.getX();
                downY = (int) event.getY();
                //判断是否已经是播放界面，如果是则不做处理，如果不是播放界面并且点击的不是bottomBar,则不拦截该点击事件，return false；
                if (!isPullUp && startY < getMeasuredHeight() - bottomBarHead.getMeasuredHeight()) {
                    Log.d(TAG, "onTouchEvent: 1");
                    return super.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int endY = (int) event.getY();
                int dy = (endY - downY);
                int toScroll = getScrollY() - dy;
                if (toScroll < 0) {
                    Log.d(TAG, "onTouchEvent: 2");
                    toScroll = 0;
                } else if (toScroll > bottomBarContent.getMeasuredHeight()) {
                    Log.d(TAG, "onTouchEvent: 3");
                    toScroll = bottomBarContent.getMeasuredHeight();
                }
                scrollTo(0, toScroll);
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (!isPullUp && startX == downX && startY == downY) {//如果为点击而不是滑动，则弹出播放界面
                    showNavigation();
                    Log.d(TAG, "onTouchEvent: 4");
                } else {
                    scrollOffset = getScrollY();

                    if (!isPullUp) {
                        Log.d(TAG, "onTouchEvent: 5");
                        if (scrollOffset > bottomBarContent.getMeasuredHeight() / 14) {
                            Log.d(TAG, "onTouchEvent: 6");
                            showNavigation();
                        } else {
                            Log.d(TAG, "onTouchEvent: 7");
                            closeNavigation();
                        }
                    } else {
                        if (scrollOffset > bottomBarContent.getMeasuredHeight() / 14 * 13) {
                            Log.d(TAG, "onTouchEvent: 8");
                            showNavigation();
                        } else {
                            Log.d(TAG, "onTouchEvent: 9");
                            closeNavigation();
                        }
                    }
                    break;
                }
        }
        return true;
    }

    /**
     * 上拉BottomBar
     *
     * @param
     * @return
     * @throws
     * @author Administrator on 2018/3/13.
     */
    public void showNavigation() {
        isPullUp = true;
        int dy = bottomBarContent.getMeasuredHeight() - scrollOffset;
        defaultScroller.startScroll(getScrollX(), getScrollY(), 0, dy, 500);
        invalidate();
        StatusBarUtil.setStatusBarDarkMode((Activity) getContext());
        setVisibilityChange(true);
        scrollOffset = 0;
    }

    /**
     * 下拉BottomBar
     *
     * @param
     * @return
     * @throws
     * @author Administrator on 2018/3/13.
     */
    public void closeNavigation() {
        isPullUp = false;
        int dy = getScrollY();
        defaultScroller.startScroll(getScrollX(), getScrollY(), 0, -dy, 500);
        invalidate();
        setVisibilityChange(false);
        scrollOffset = 0;
    }

    /**
     * 对外提供此时bottombar的状态
     *
     * @param
     * @return boolean isPullUp
     * @throws
     * @author CNT on 2018/1/31.
     */
    public boolean getIsPullUp() {
        return isPullUp;
    }

    /**
     * 对外提供下拉播放页的功能，调用则播放页下拉恢复
     *
     * @param
     * @return
     * @throws
     * @author CNT on 2018/1/31.
     */
    public void pullDown() {
        if (isPullUp) {
            Log.e(TAG, "pullDown: uiuiiu");
            closeNavigation();
            setVisibilityChange(false);
        }
    }

    public void pullUp() {
        if (!isPullUp) {
            defaultScroller.startScroll(0, 0, 0, bottomBarContent.getMeasuredHeight(), 500);
            invalidate();
            isPullUp = true;
            setVisibilityChange(true);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bottomBarHead.layout(0, getMeasuredHeight() - bottomBarHead.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
        bottomBarContent.layout(0, getMeasuredHeight(), getMeasuredWidth(), bottomBarHead.getBottom() + bottomBarContent.getMeasuredHeight());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (defaultScroller.computeScrollOffset()) {
            scrollTo(defaultScroller.getCurrX(), defaultScroller.getCurrY());
            invalidate();
        }

        if (scrollerListener != null)
            scrollerListener.upDate(getScrollY());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(touchHandle);
        Log.d(TAG, "onDetachedFromWindow: 死亡");
    }

    /**
     * 上下拉的监听
     *
     * @param
     * @author BA on 2018/2/26 0026
     * @return
     * @exception
     */
    public interface OnUpOrDownListener {
        public void statusChange(boolean isUp);
    }

    /**
     * 设置可见状态改变
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/26 0026
     */
    public void setVisibilityChange(boolean isUp) {
        if (onUpOrDownListener != null)
            onUpOrDownListener.statusChange(isUp);
    }


    /**
     * 设置触摸事件
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/25 0025
     */
    public void setTouchHandle(BottomBarHandle touchHandle) {
        this.touchHandle = touchHandle;
        touchHandle.setContentView(this);
    }

    /**
     * 设置上下拉状态的监听
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/26 0026
     */
    public void setOnUpOrDownListener(BottomBar.OnUpOrDownListener onUpOrDownListener) {
        this.onUpOrDownListener = onUpOrDownListener;
    }

    /**
     * 设置滑动监听
     *
     * @param
     * @return
     * @throws
     * @author Administrator on 2018/3/13.
     */
    public void setScrollerListener(OnScrollerListener scrollerListener) {
        this.scrollerListener = scrollerListener;
    }
}


