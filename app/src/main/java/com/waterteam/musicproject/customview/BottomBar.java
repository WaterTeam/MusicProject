package com.waterteam.musicproject.customview;

import android.app.Activity;
import android.content.Context;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import android.widget.FrameLayout;

import android.widget.Scroller;


import com.waterteam.musicproject.R;
import com.waterteam.musicproject.util.HandleBottomBarTouchUtil;
import com.waterteam.musicproject.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by CNT on 2018/1/29.
 * <p>
 * 自定义ViewGroup:BottomBar,实现底部控件上拉后，处于底部控件下的（未显示的）控件得以显现；
 * 下拉则恢复；点击底部控件则底部控件下的（未显示的）控件填充未显示控件的高度
 */

public class BottomBar extends FrameLayout {
    private static final String TAG = "BottomBar";
    public View bottomBar;

    public View bottomContent;

    private SecondBottomBar secondBottomBar;
    private View secondBottomBarHead;
    private View secondBottomBarComtent;

    private Scroller mScroller;

    private int downX, downY, startX, startY;
    private int scrollOffset;

    //判断是否为播放界面
    private boolean isPullUp = false;

    //触摸监听
    private BottomBarTouchListener touchListener;

    //上下拉状态改变监听
    private VisibilityListener visibilityListener;

    public boolean isSecond = false;//由于要用到2个bottomBar嵌套，可是需要第二个bottomBar不去影响到第一个bottomBar设定的状态栏的颜色，所以才用这个参数控制

    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*
      控制栏的可视范围
     */
        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
        Log.d(TAG, "onFinishInflate: ");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");

    }

    private void initView() {
        bottomBar = getChildAt(0);
        bottomContent = getChildAt(1);
        secondBottomBar = (SecondBottomBar) bottomContent.findViewById(R.id.second_bottomBar);
        secondBottomBarHead = bottomContent.findViewById(R.id.second_bottomBarHead);
        secondBottomBarComtent = bottomContent.findViewById(R.id.second_bottomBarContent);

    }

    /**
     * 设置触摸事件
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/25 0025
     */
    public void setTouchListener(BottomBarTouchListener listener) {
        touchListener = listener;
        touchListener.setContentView(this);
    }

    /**
     * 设置上下拉状态的监听
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/26 0026
     */
    public void setVisibilityListener(VisibilityListener visibilityListener) {
        this.visibilityListener = visibilityListener;
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
                if (!isPullUp && startY < getMeasuredHeight() - bottomBar.getMeasuredHeight()) {
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
                } else if (toScroll > bottomContent.getMeasuredHeight()) {
                    Log.d(TAG, "onTouchEvent: 3");
                    toScroll = bottomContent.getMeasuredHeight();
                }
                scrollTo(0, toScroll);
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (!isPullUp && startX == downX && startY == downY) {//如果为点击而不是滑动，则弹出播放界面
                    mScroller.startScroll(0, 0, 0, bottomContent.getMeasuredHeight(), 500);
                    isPullUp = true;
                    invalidate();
                    StatusBarUtil.setStatusBarDarkMode((Activity) getContext());
                    setVisilityChange(true);
                    Log.d(TAG, "onTouchEvent: 4");
                } else {
                    scrollOffset = getScrollY();

                    if (!isPullUp) {
                        Log.d(TAG, "onTouchEvent: 5");
                        if (scrollOffset > bottomContent.getMeasuredHeight() / 16) {
                            Log.d(TAG, "onTouchEvent: 6");
                            showNavigation();
                            isPullUp = true;
                        } else {
                            Log.d(TAG, "onTouchEvent: 7");
                            closeNavigation();
                            isPullUp = false;
                        }
                    } else {
                        if (scrollOffset > bottomContent.getMeasuredHeight() / 16 * 15) {
                            Log.d(TAG, "onTouchEvent: 8");
                            showNavigation();
                            isPullUp = true;
                        } else {
                            Log.d(TAG, "onTouchEvent: 9");
                            closeNavigation();
                            isPullUp = false;
                        }
                    }
                    break;
                }
        }
        return true;
    }

    private void showNavigation() {
        int dy = bottomContent.getMeasuredHeight() - scrollOffset;
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, 500);
        invalidate();
        StatusBarUtil.setStatusBarDarkMode((Activity) getContext());
        setVisilityChange(true);
    }

    private void closeNavigation() {

        int dy = 0 - scrollOffset;
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, 500);
        invalidate();
        if (!isSecond) {
            StatusBarUtil.setStatusBarLightMode((Activity) getContext());
        }
        setVisilityChange(false);
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
            mScroller.startScroll(0, bottomContent.getMeasuredHeight(), 0, -bottomContent.getMeasuredHeight(), 500);
            invalidate();
            isPullUp = false;
            StatusBarUtil.setStatusBarLightMode((Activity) getContext());
            setVisilityChange(false);
        }
    }

    public void pullUp() {
        if (!isPullUp) {
            mScroller.startScroll(0, 0, 0, bottomContent.getMeasuredHeight(), 500);
            invalidate();
            isPullUp = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bottomBar.layout(0, getMeasuredHeight() - bottomBar.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
        bottomContent.layout(0, getMeasuredHeight(), getMeasuredWidth(), bottomBar.getBottom() + bottomContent.getMeasuredHeight());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(touchListener);
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
    public interface VisibilityListener {
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
    public void setVisilityChange(boolean isUp) {
        if (visibilityListener != null)
            visibilityListener.statusChange(isUp);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean isIntercept = false;
        downX = (int) e.getX();
        downY = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) e.getX();
                startY = (int) e.getY();
                downX = (int) e.getX();
                downY = (int) e.getY();
                isIntercept = false;//点击事件分发给子控件
                break;
            case MotionEvent.ACTION_MOVE:
                int Y = bottomContent.getMeasuredHeight() - secondBottomBarHead.getMeasuredHeight();
                int deltaY = downY - startY;
                int delaX = downX - startX;
                if (isPullUp) {
                    if (secondBottomBar.getIsPullUp()) {
                        isIntercept = false;
                    } else {
                        if (startY < Y) {
                            if (Math.abs(deltaY) > 1 && Math.abs(deltaY) > Math.abs(delaX)) {
                                isIntercept = true;
                            }
                        } else {
                            isIntercept = false;
                        }
                    }
                } else {
                    if (Math.abs(deltaY) > 1 && Math.abs(deltaY) > Math.abs(delaX)) {
                        isIntercept = true;
                    } else {
                        isIntercept = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isIntercept = false;//点击事件分发给子控件
                break;
        }
        return isIntercept;

    }
}
