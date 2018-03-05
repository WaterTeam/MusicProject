package com.waterteam.musicproject.customview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import com.waterteam.musicproject.util.StatusBarUtil;

/**
 * Created by Administrator on 2018/3/1.
 */

public class ButtonOnSecondBottom extends AppCompatButton {
    private static final String TAG = "ButtonOnSecondBottom";
    private BottomBar secondBottom;
    private int UX = 0, UY = 0, downX = 0, downY = 0;
    private boolean isFirstTouch = true;

    public ButtonOnSecondBottom(Context context) {
        super(context);
    }

    public ButtonOnSecondBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
        Toast.makeText(getContext(), "onTouchEvent", Toast.LENGTH_SHORT).show();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(getContext(), "down", Toast.LENGTH_SHORT).show();
                downX = (int) event.getX();
                downY = (int) event.getY();
                return true;

            case MotionEvent.ACTION_MOVE:
                Toast.makeText(getContext(), "move", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onTouchEvent:move ");
                return false;
            case MotionEvent.ACTION_UP:
                Toast.makeText(getContext(), "up", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onTouchEvent: up");
                UX = (int) event.getX();
                UY = (int) event.getY();
                if (secondBottom != null) {
                    if (secondBottom.getIsPullUp()) {
                        if (UX == downX &&downY == UY) {
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
