package com.waterteam.musicproject.eventsforeventbus;

import com.waterteam.musicproject.bean.SongsBean;

import java.util.List;

/**
 * 音乐播放界面（bottomBar）所需的数据，用于设置bottomBar的界面控件内容
 * <p>
 * Created by CNT on 2018/2/3.
 */

public class PlayingBarEvent {

    public static final int PLAYTOPAUSE = 1;
    public static final int PAUSETOPLAY = 2;
    public static final int PLAYANEW = 3;
    public static final int PLAYNEXT = 4;
    public static final int PLAYLAST = 5;

    private int playingStatus;
    private List<SongsBean> songsBeanList;
    private int position;

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


    public int getPlayingStatus() {
        return playingStatus;
    }

    public void setPlayingStatus(int playingStatus) {
        this.playingStatus = playingStatus;
    }

}
