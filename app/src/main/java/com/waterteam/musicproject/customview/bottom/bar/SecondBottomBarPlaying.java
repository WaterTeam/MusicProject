package com.waterteam.musicproject.customview.bottom.bar;

import android.app.Activity;
import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import android.view.ViewGroup;

import android.widget.Scroller;


import com.waterteam.musicproject.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**继承于一个个bottomBar，只服务于镶嵌在bottomBar中的第二个bottomBar，实现了如果是点击则传给子控件，如果是按住移动则自身拦截，用于上下移动bottomBar
 * Created by CNT on 2018/1/29.
 *
 */

public class SecondBottomBarPlaying extends BottomBar {
    private static final String TAG = "BottomBar";


    private int downX, downY, startX, startY;


    public boolean isMyRecycleView = false;




    public SecondBottomBarPlaying(Context context) {
        this(context, null);
    }

    public SecondBottomBarPlaying(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*
      控制栏的可视范围
     */
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
                int Y = getMeasuredHeight() - bottomBarContent.getMeasuredHeight() + 45;
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

    @Override
    public void showNavigation() {
        super.showNavigation();
        isMyRecycleView = false;
    }

    @Override
    public void closeNavigation() {
        super.closeNavigation();
        isMyRecycleView = false;
    }

    public void pullDown() {
        super.pullDown();
        isMyRecycleView = false;
    }

    public void pullUp() {
       super.pullUp();
        isMyRecycleView = false;
    }




}

