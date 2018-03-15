package com.waterteam.musicproject.eventsforeventbus;

/**
 * Created by Administrator on 2018/3/15.
 */

public class EventFromNotification {
    private int statu;

    public final static int PAUSE = 9;
    public final static int PAUSETOPLAY = 4;
    public final static int PLAYNEXT = 5;
    public final static int PLAYLAST = 6;

    public final static int LISTMODE = 0;//列表循环
    public final static int SIMPLEMODE = 1;//单曲循环
    public final static int RANDOMMODE = 2;//随机模式



    public int getStatu(){return statu;}

    public void setStatu(int statu){
        this.statu=statu;
    }

    /**
     * 当拖动seekbar的时候需要将拖动的进度传给服务
     * @author BA on 2018/2/7 0007
     * @param
     * @return
     * @exception
     */
}
