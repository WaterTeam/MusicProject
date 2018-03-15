package com.waterteam.musicproject.customview.playing_viewpage;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/3/15.
 */

public class MyViewPager extends ViewPager{
    private boolean isUserMove;
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            isUserMove = true;
        }
        return super.onTouchEvent(ev);
    }
    public boolean getIsUserMove(){
        return isUserMove;
    }
    public void setIsUserMove(boolean isUserMove){
        this.isUserMove = isUserMove;
    }
}
