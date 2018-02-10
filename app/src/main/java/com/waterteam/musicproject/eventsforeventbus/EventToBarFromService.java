package com.waterteam.musicproject.eventsforeventbus;

import com.waterteam.musicproject.bean.SongsBean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/8.
 */

public class EventToBarFromService {
    public List<SongsBean> getSongsBeanList() {
        return songsBeanList;
    }

    public void setSongsBeanList(List<SongsBean> songsBeanList) {
        this.songsBeanList = songsBeanList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }



    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }



    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    private List<SongsBean> songsBeanList;
    private int position;
    private int statu;
    private int progress;

    public int getPlayMode() {
        return playMode;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    private int playMode = -1;

    public final static int PLAYANEW = 1;
    public final static int PAUSE = 2;
    public final static int PAUSETOPLAY = 4;
    public final static int MOVESEEKBAR = 5;
    public final static int SEEKBARMOVEITSELF = 6;
}
