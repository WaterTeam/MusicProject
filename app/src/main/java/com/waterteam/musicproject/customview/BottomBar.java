package com.waterteam.musicproject.customview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by CNT on 2018/1/29.
 * <p>
 * 自定义ViewGroup:BottomBar,实现底部控件上拉后，处于底部控件下的（未显示的）控件得以显现；
 * 下拉则恢复；点击底部控件则底部控件下的（未显示的）控件填充整个屏幕
 */

public class BottomBar extends FrameLayout {
    private View bottomBar;

    private View bottomContent;

    private Scroller mScroller;

    /**
     * 控制栏的可视范围
     */
    private Rect barRect;

    private int downX, downY, startX, startY;
    private int scrollOffset;

    //判断是否为播放界面
    private boolean isPullUp = false;

    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initView();
    }

    private void initView() {
        int count = getChildCount();
        if (count != 2) {
        }
        bottomBar = getChildAt(0);
        bottomContent = getChildAt(1);
        barRect = new Rect();
        mScroller = new Scroller(getContext());
        //bottomBar.getGlobalVisibleRect(barRect);
        //bottomContent.getGlobalVisibleRect(barRect);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                downX = (int) event.getX();
                downY = (int) event.getY();
                //判断是否已经是播放界面，如果是则不做处理，如果不是播放界面并且点击的不是bottomBar,则不拦截该点击事件，return false；
                if (isPullUp) {
                } else if (!isPullUp && startY < getMeasuredHeight() - bottomBar.getMeasuredHeight()) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int endY = (int) event.getY();
                int dy = (int) (endY - downY);
                int toScroll = getScrollY() - dy;
                if (toScroll < 0) {
                    toScroll = 0;
                } else if (toScroll > bottomContent.getMeasuredHeight()) {
                    toScroll = bottomContent.getMeasuredHeight();
                }
                scrollTo(0, toScroll);
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if ( !isPullUp && startX == downX && startY == downY) {//如果为点击而不是滑动，则弹出播放界面
                    mScroller.startScroll(0, 0, 0, getMeasuredHeight(), 500);
                    isPullUp = true;
                    invalidate();
                } else {
                    scrollOffset = getScrollY();
                    if (!isPullUp) {
                        if (scrollOffset > bottomContent.getMeasuredHeight() / 8) {
                            showNavigation();
                            isPullUp = true;
                        } else {
                            closeNavigation();
                            isPullUp = false;
                        }
                    } else {
                        if (scrollOffset > bottomContent.getMeasuredHeight() / 8 * 7) {
                            showNavigation();
                            isPullUp = true;
                        } else {
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
    }

    private void closeNavigation() {
        int dy = 0 - scrollOffset;
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, 500);
        invalidate();
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
}
