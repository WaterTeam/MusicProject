package com.waterteam.musicproject.customview;

import android.app.Activity;
import android.content.Context;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import android.view.ViewGroup;
import android.widget.FrameLayout;

import android.widget.Scroller;
import android.widget.SeekBar;


import com.waterteam.musicproject.R;
import com.waterteam.musicproject.util.HandleBottomBarTouchUtil;
import com.waterteam.musicproject.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**继承于一个个bottomBar，只服务于镶嵌在bottomBar中的第二个bottomBar，实现了如果是点击则传给子控件，如果是按住移动则自身拦截，用于上下移动bottomBar
 * Created by CNT on 2018/1/29.
 *
 */

public class SecondBottomBar extends BottomBar {
    private static final String TAG = "BottomBar";
    public View bottomBar;

    public View bottomContent;
    private View view;

    private Scroller mScroller;

    private int downX, downY, startX, startY;
    private int scrollOffset;

    //判断是否为播放界面
    private boolean isPullUp = false;

    public boolean isMyRecycleView = false;

    //触摸监听
    private BottomBarTouchListener touchListener;

    //上下拉状态改变监听
    private VisibilityListener visibilityListener;


    public SecondBottomBar(Context context) {
        this(context, null);
    }

    public SecondBottomBar(Context context, AttributeSet attrs) {
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
                        if (scrollOffset > bottomContent.getMeasuredHeight() / 8) {
                            Log.d(TAG, "onTouchEvent: 6");
                            showNavigation();
                            isPullUp = true;
                        } else {
                            Log.d(TAG, "onTouchEvent: 7");
                            closeNavigation();
                            isPullUp = false;
                        }
                    } else {
                        if (scrollOffset > bottomContent.getMeasuredHeight() / 8 * 7) {
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean isIntercept = false;
        downX = (int) e.getX();
        downY = (int) e.getY();
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = (int) e.getX();
                startY = (int) e.getY();
                downX = (int) e.getX();
                downY = (int) e.getY();
                isIntercept = false;//点击事件分发给子控件
                break;
            case MotionEvent.ACTION_MOVE:
                int Y = getMeasuredHeight() - bottomContent.getMeasuredHeight() + 45;
                int deltaY = downY - startY;
                int delaX = downX - startX;
                if(isMyRecycleView){
                    if (Math.abs(deltaY) > 1 && Math.abs(deltaY) > Math.abs(delaX)) {
                        isIntercept = true;
                    }
                }else{
                    if(!isPullUp){
                        if (Math.abs(deltaY) > 1 && Math.abs(deltaY) > Math.abs(delaX)) {
                            isIntercept = true;
                        }
                    }else if(startY < Y){
                        downX = (int) e.getX();
                        downY = (int) e.getY();
                        if (Math.abs(deltaY) > 1 && Math.abs(deltaY) > Math.abs(delaX)) {
                            isIntercept = true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isMyRecycleView =false;
                isIntercept =false;//点击事件分发给子控件
                break;
        }
            return isIntercept;

    }

    private void showNavigation() {
        int dy = bottomContent.getMeasuredHeight() - scrollOffset;
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, 500);
        invalidate();
        StatusBarUtil.setStatusBarDarkMode((Activity) getContext());
        setVisilityChange(true);
        isMyRecycleView = false;
    }

    private void closeNavigation() {

        int dy = 0 - scrollOffset;
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, 500);
        invalidate();
            StatusBarUtil.setStatusBarLightMode((Activity) getContext());
        setVisilityChange(false);
        isMyRecycleView = false;
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
            isMyRecycleView = false;
        }
    }

    public void pullUp() {
        if (!isPullUp) {
            mScroller.startScroll(0, 0, 0, bottomContent.getMeasuredHeight(), 500);
            invalidate();
            isPullUp = true;
            isMyRecycleView = false;
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

    /**
     * 根据坐标获取相对应的子控件<br>
     * 在重写ViewGroup使用
     *
     * @param
     * @param
     * @return 目标View
     */
    public View getViewAtViewGroup(int x, int y) {
        return findViewByXY(this, x, y);
    }

    private View findViewByXY(View view, int x, int y) {
        View targetView = null;
        if (view instanceof BottomBar) {
            // 父容器,遍历子控件
            ViewGroup v = (ViewGroup) view;
            for (int i = 0; i < v.getChildCount(); i++) {
                targetView = findViewByXY(v.getChildAt(i), x, y);
                if (targetView != null) {
                    break;
                }
            }
        } else {
            targetView = getTouchTarget(view, x, y);
        }
        return targetView;

    }

    private View getTouchTarget(View view, int x, int y) {
        View targetView = null;
        // 判断view是否可以聚焦
        ArrayList<View> TouchableViews = view.getTouchables();
        for (View child : TouchableViews) {
            if (isTouchPointInView(child, x, y)) {
                targetView = child;
                break;
            }
        }
        return targetView;
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if (view.isClickable() && y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }
}
