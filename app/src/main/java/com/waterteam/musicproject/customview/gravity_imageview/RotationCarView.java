package com.waterteam.musicproject.customview.gravity_imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.waterteam.musicproject.bean.GetCoverUri;
import com.waterteam.musicproject.util.GetCoverUtil;

/**
 * Created by BA on 2018/3/8 0008.
 *
 * @Function : 实现根据重力翻转的View
 */

public class RotationCarView extends CardView {
    private static final String TAG = "RotationCarView";
    private ImageView imageView;
    private GetCoverUri bean;
    private boolean isAnimator;

    private Camera camera;
    private Matrix matrix;

    private float maxRotation = 5;

    private float x, y;

    public RotationCarView(Context context) {
        super(context);
    }

    public RotationCarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix = new Matrix();
        camera = new Camera();
    }

    public RotationCarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        imageView = (ImageView) getChildAt(0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        GetCoverUtil.setCover(getContext(),bean,imageView,400);
        if (isAnimator) {
            camera.save();
            //绕X轴翻转
            camera.rotateX(0);
            //绕Y轴翻转
            camera.rotateY(x);
            //设置camera作用矩阵
            camera.getMatrix(matrix);
            camera.restore();
            //设置翻转中心点
            matrix.preTranslate(-getMeasuredWidth() / 2, -getMeasuredHeight() / 2);
            matrix.postTranslate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
            canvas.save();
            canvas.concat(matrix);
        }
        super.onDraw(canvas);
    }

    public void setGyroscopeObserver(MySensorObserver observer) {
        if (observer != null) {
            observer.addPanoramaImageView(this);
        }
    }

    void updateProgress(float pX, float pY) {
        x = pX * maxRotation;
        y = pY * maxRotation;
        invalidate();
    }

    public void setBean(GetCoverUri bean) {
        this.bean = bean;
    }

    public void setAnimator(boolean isAnimator) {
        this.isAnimator = isAnimator;
    }
}
