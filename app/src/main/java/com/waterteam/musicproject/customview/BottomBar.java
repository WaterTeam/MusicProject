package com.waterteam.musicproject.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.WindowManager;
import android.widget.FrameLayout;

import android.widget.Scroller;




import com.waterteam.musicproject.util.HandleBottomBarTouchUtil;

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

    private int downX, downY, startX, startY;
    private int scrollOffset;


    //判断是否为播放界面
    private boolean isPullUp = false;

    public HandleBottomBarTouchUtil getHandleBottomBarTouchUtil() {
        return handleBottomBarTouchUtil;
    }

    HandleBottomBarTouchUtil handleBottomBarTouchUtil;

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initView();
    }

    private void initView() {
        bottomBar = getChildAt(0);
        bottomContent = getChildAt(1);

        handleBottomBarTouchUtil = new HandleBottomBarTouchUtil(bottomBar,bottomContent);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                downX = (int) event.getX();
                downY = (int) event.getY();
                //判断是否已经是播放界面，如果是则不做处理，如果不是播放界面并且点击的不是bottomBar,则不拦截该点击事件，return false；
               if (!isPullUp && startY < getMeasuredHeight() - bottomBar.getMeasuredHeight()) {
                   return super.onTouchEvent(event);
               }
                break;
            case MotionEvent.ACTION_MOVE:
                int endY = (int) event.getY();
                int dy = (endY - downY);
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
                if (!isPullUp && startX == downX && startY == downY) {//如果为点击而不是滑动，则弹出播放界面
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
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy+getStatusBarHeigth(), 500);
        invalidate();
        setFullScreen();
    }

    private void closeNavigation() {
        int dy = 0 - scrollOffset;
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, 500);
        invalidate();
        quitFullScreen();
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


    private void setFullScreen(){
        ((Activity)getContext()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private void quitFullScreen(){
        final WindowManager.LayoutParams attrs = ((Activity)getContext()).getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity)getContext()).getWindow().setAttributes(attrs);
        ((Activity)getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * function : 获取状态栏高度
     * param :
     * return : 返回获取到的状态栏高度，没有获取到就返回-1
     * exception :
     */
    public int getStatusBarHeigth() {
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
