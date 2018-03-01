package com.waterteam.musicproject.customview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import com.waterteam.musicproject.util.StatusBarUtil;

/**
 * Created by Administrator on 2018/3/1.
 */

public class ButtonOnSecondBottom extends AppCompatButton {
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
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (secondBottom != null) {
                    if (!secondBottom.getIsPullUp()) {
                        return false;
                    } else {
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        downX = (int) event.getX();
                        downY = (int) event.getY();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                return false;
            case MotionEvent.ACTION_UP:
                downX = (int) event.getX();
                downY = (int) event.getY();
                if (secondBottom != null) {
                    if (secondBottom.getIsPullUp()) {
                        if (downX == startX && downY == startY) {
                            secondBottom.pullDown();
                            StatusBarUtil.setStatusBarDarkMode((Activity) secondBottom.getContext());
                            return true;
                        }
                    }
                }
                break;
        }
        return true;
    }

    public void setSecondBottom(BottomBar bottomBar) {
        secondBottom = bottomBar;
    }
}
