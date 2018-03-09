package com.waterteam.musicproject.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import com.waterteam.musicproject.service.playmusic.service.PlayService;

/**
 * 这个RecycleView只服务于第二个bottomBar，使用时要传第二个bottomBar参数进去；
 * Created by CNT on 2018/3/6.
 */

public class MyRecycleView extends RecyclerView {
    private int state = -1;

    private int startX, startY, endX, endY;

    private SecondBottomBar secondBottomBar;

    public MyRecycleView(Context context) {
        super(context);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) e.getX();
                startY = (int) e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                RecyclerView.LayoutManager layoutManager = this.getLayoutManager();
                if(PlayService.playList.getSongs().size()<5){
                    secondBottomBar.isMyRecycleView = true;
                    return false;
                }
                //判断是当前layoutManager是否为LinearLayoutManager
                //只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    endX = (int) e.getX();
                    endY = (int) e.getY();
                    int deltaY = endY - startY;
                    //int delaX = endX - startX;
                    if (deltaY > 1) {
                        //同理检测是否为顶部itemView时,只需要判断其位置是否为0即可
                        if (state == RecyclerView.SCROLL_STATE_IDLE && firstItemPosition == 0) {
                            if (secondBottomBar != null) {
                                secondBottomBar.isMyRecycleView = true;
                            }
                            return false;
                        }
                    }
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    public void setBottomBar(SecondBottomBar secondBottomBar) {
        this.secondBottomBar = secondBottomBar;
    }

    @Override
    public void onScrollStateChanged(int state) {
        this.state = state;
        super.onScrollStateChanged(state);

    }

}
