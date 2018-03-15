package com.waterteam.musicproject.customview.bottom.bar;

import android.app.Activity;
import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.waterteam.musicproject.R;
import com.waterteam.musicproject.util.StatusBarUtil;

/**
 * Created by CNT on 2018/1/29.
 * <p>
 * 自定义ViewGroup:BottomBar,实现底部控件上拉后，处于底部控件下的（未显示的）控件得以显现；
 * 下拉则恢复；点击底部控件则底部控件下的（未显示的）控件填充未显示控件的高度
 */

public class BottomBarPlaying extends BottomBar {
    private static final String TAG = "BottomBar";

    private BottomBarPlayingControl secondBottomBar;
    private View secondBottomBarHead;
    private View secondBottomBarContent;

    //判断手指按下的位置
    private int downX, downY, startX, startY;


    public BottomBarPlaying(Context context) {
        this(context, null);
    }

    public BottomBarPlaying(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*
      控制栏的可视范围
     */

    }


    @Override
    public void initView() {
        super.initView();
        secondBottomBar = (BottomBarPlayingControl) bottomBarContent.findViewById(R.id.second_bottomBar);
        secondBottomBarHead = bottomBarContent.findViewById(R.id.second_bottomBarHead);
        secondBottomBarContent = bottomBarContent.findViewById(R.id.second_bottomBarContent);
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

        if (secondBottomBar.getIsPullUp()) {
            secondBottomBar.pullDown();
        } else {
            if (isPullUp) {
                Log.e(TAG, "pullDown: uiuiiu");
                closeNavigation();
                StatusBarUtil.setStatusBarLightMode((Activity) getContext());

            }
        }
    }

    @Override
    public void showNavigation() {
        super.showNavigation();
        StatusBarUtil.setStatusBarDarkMode((Activity) getContext());
    }

    @Override
    public void closeNavigation() {
        super.closeNavigation();
        StatusBarUtil.setStatusBarLightMode((Activity) getContext());
    }


    public void pullUp() {
        super.pullUp();
        StatusBarUtil.setStatusBarDarkMode((Activity) getContext());
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
                int Y = bottomBarContent.getMeasuredHeight() - secondBottomBarHead.getMeasuredHeight();
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
