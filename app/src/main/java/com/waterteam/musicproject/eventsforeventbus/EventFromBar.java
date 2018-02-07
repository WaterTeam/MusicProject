package com.waterteam.musicproject.eventsforeventbus;

/**
 * Created by BA on 2018/2/6 0006.
 *
 * @Function :
 */

public class EventFromBar {
    private int statu;

    public final static int PLAY = 1;
    public final static int PAUSE = 2;
    public final static int STOP = 3;
    public final static int PAUSETOPLAY = 4;
    public final static int PLAYNEXT = 5;
    public final static int PLAYLAST = 6;
    public int progress; //歌曲进度

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
    public void setProgress(int progress){
        this.progress=progress;
    }
}
