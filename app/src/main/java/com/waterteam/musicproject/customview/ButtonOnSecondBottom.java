package com.waterteam.musicproject.customview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.waterteam.musicproject.util.StatusBarUtil;

/**
 * Created by Administrator on 2018/3/1.
 */

    public class ButtonOnSecondBottom extends FrameLayout {

    private static final String TAG = "ButtonOnSecondBottom";
    private BottomBar secondBottom;
    private int startX = 0, startY = 0, downX = 0, downY = 0;
    private boolean isFirstTouch = true;

    public ButtonOnSecondBottom(Context context) {
        super(context);
    }

    public ButtonOnSecondBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent: ");
        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (secondBottom != null) {
                    if (!secondBottom.getIsPullUp()) {
                        Log.d(TAG, "onTouchEvent: down isPull");
                        return true;
                    } else {
                        Log.d(TAG, "onTouchEvent: down else");
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        downX = (int) event.getX();
                        downY = (int) event.getY();
                    }
                }
            break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: move");
               break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: up");
                downX = (int) event.getX();
                downY = (int) event.getY();
                if (secondBottom != null) {
                    if (secondBottom.getIsPullUp()) {
                        if (downX == startX && downY == startY) {
                            Log.d(TAG, "onTouchEvent: up_down");
                            secondBottom.pullDown();
                            StatusBarUtil.setStatusBarDarkMode((Activity) secondBottom.getContext());
                            return true;
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setSecondBottom(BottomBar bottomBar) {
        secondBottom = bottomBar;
    }
}
