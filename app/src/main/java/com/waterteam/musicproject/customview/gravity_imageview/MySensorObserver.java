package com.waterteam.musicproject.customview.gravity_imageview;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.LinkedList;

/**
 * Created by BA on 2018/2/25 0025.
 *
 * @Function : 观察者的管理，用来监听传感器变化，计算出需要移动的大小
 */

public class MySensorObserver implements SensorEventListener {
    private static final String TAG = "MySensorObserver";
    private SensorManager mSensorManager;

    // For translate nanosecond to second.纳秒转换成秒
    private static final float NS2S = 1.0f / 1000000000.0f;

    // The time in nanosecond when last sensor event happened.
    private long mLastTimestamp;

    // The radian the device already rotate along y-axis.
    private double mRotateRadianY;

    // The radian the device already rotate along x-axis.
    private double mRotateRadianX;

    // The maximum radian that the device can rotate along x-axis and y-axis.
    // The value must between (0, π/2].
    private double mMaxRotateRadian = Math.PI/9;

    // The PanoramaImageViews to be notified when the device rotate.
    private LinkedList<MyGravityImageView> mViews = new LinkedList<>();

    public void register(Context context) {
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);

        mLastTimestamp = 0;
        mRotateRadianY = mRotateRadianX = 0;
    }

    public void unregister() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
            mSensorManager = null;
        }
    }

    void addPanoramaImageView(MyGravityImageView view) {
        if (view != null && !mViews.contains(view)) {
            mViews.addFirst(view);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mLastTimestamp == 0) {
            mLastTimestamp = event.timestamp;
            return;
        }

        float rotateX = Math.abs(event.values[0]);
        float rotateY = Math.abs(event.values[1]);
        float rotateZ = Math.abs(event.values[2]);

        final float dT = (event.timestamp - mLastTimestamp) * NS2S;
        mRotateRadianY += event.values[1] * dT;
        mRotateRadianX += event.values[0] * dT;
        if (mRotateRadianY > mMaxRotateRadian) {
            mRotateRadianY = mMaxRotateRadian;
        } else if (mRotateRadianY < -mMaxRotateRadian) {
            mRotateRadianY = -mMaxRotateRadian;
        } if (mRotateRadianX > mMaxRotateRadian) {
            mRotateRadianX = mMaxRotateRadian;
        } else if (mRotateRadianX < -mMaxRotateRadian) {
            mRotateRadianX = -mMaxRotateRadian;
        } else {
            for (MyGravityImageView view: mViews) {
                if (view != null ) {
                    view.updateProgress((float) (mRotateRadianY / mMaxRotateRadian),(float) (mRotateRadianX / mMaxRotateRadian));
                }
            }
        }
        mLastTimestamp = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setMaxRotateRadian(double maxRotateRadian) {
        if (maxRotateRadian <= 0 || maxRotateRadian > Math.PI/2) {
            throw new IllegalArgumentException("The maxRotateRadian must be between (0, π/2].");
        }
        this.mMaxRotateRadian = maxRotateRadian;
    }
}
