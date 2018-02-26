package com.waterteam.musicproject.customview.gravity_imageview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;


/**
 * Created by BA on 2018/2/25 0025.
 *
 * @Function : 实现重力感应imageView
 */

public class MyGravityImageView extends android.support.v7.widget.AppCompatImageView {

    // Image's scroll orientation
    public final static byte ORIENTATION_NONE = -1;
    public final static byte ORIENTATION_HORIZONTAL = 0;
    public final static byte ORIENTATION_VERTICAL = 1;
    private byte mOrientation = ORIENTATION_NONE;


    // Image's width and height
    private int mDrawableWidth;
    private int mDrawableHeight;

    // View's width and height
    private int mWidth;
    private int mHeight;

    // Image's offset from initial state(center in the view).
    private float mMaxOffsetX, mMaxOffsetY;

    // The scroll progress.
    private float mPX, mPY;


    // Observe scroll state
    private OnPanoramaScrollListener mOnPanoramaScrollListener;

    public MyGravityImageView(Context context) {
        this(context, null);
    }

    public MyGravityImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGravityImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setScaleType(ScaleType.CENTER_CROP);
    }

    public void setGyroscopeObserver(MySensorObserver observer) {
        if (observer != null) {
            observer.addPanoramaImageView(this);
        }
    }

    void updateProgress(float pX, float pY) {
        mPX = pX;
        mPY = pY;
        invalidate();
        if (mOnPanoramaScrollListener != null) {
            mOnPanoramaScrollListener.onScrolled(this, -mPY,-mPY);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        mHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        if (getDrawable() != null) {
            mDrawableWidth = getDrawable().getIntrinsicWidth();
            mDrawableHeight = getDrawable().getIntrinsicHeight();

            float imgScaleY = (float) mWidth / (float) mDrawableWidth;
            float imgScaleX = (float) mHeight / (float) mDrawableHeight;

            mMaxOffsetX = Math.abs((mDrawableWidth * imgScaleX - mWidth) * 0.5f);
            mMaxOffsetY = Math.abs((mDrawableHeight * imgScaleY - mHeight) * 0.5f);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float currentOffsetX = mMaxOffsetX * mPX;
        float currentOffsetY = mMaxOffsetY * mPY;
        canvas.save();
        canvas.translate(currentOffsetX, currentOffsetY);
        super.onDraw(canvas);
        canvas.restore();
    }


    @Override
    public void setScaleType(ScaleType scaleType) {
        /**
         * Do nothing because PanoramaImageView only
         * supports {@link scaleType.CENTER_CROP}
         */
    }

    /**
     * Interface definition for a callback to be invoked when the image is scrolling
     */
    public interface OnPanoramaScrollListener {
        void onScrolled(MyGravityImageView view, float offsetProgressX, float offsetProgressY);
    }

    public void setOnPanoramaScrollListener(OnPanoramaScrollListener listener) {
        mOnPanoramaScrollListener = listener;
    }
}

