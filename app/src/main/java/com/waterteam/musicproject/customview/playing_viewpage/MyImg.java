package com.waterteam.musicproject.customview.playing_viewpage;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by cnt on 2018/3/23.
 */

public class MyImg extends ImageView {
    public MyImg(Context context) {
        super(context);
    }

    public MyImg(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("PlayingImgFragment", "onDraw: ");
    }
}
