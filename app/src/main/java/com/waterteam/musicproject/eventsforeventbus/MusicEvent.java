package com.waterteam.musicproject.eventsforeventbus;

/**
 * Created by Administrator on 2018/2/1.
 */

import com.waterteam.musicproject.bean.SongsBean;

import java.util.List;

/**
 * 用于EventBus的一个事件类，用于于PlayMusicService服务，使用时设置想要的播放情况（播放，暂停，停止，暂停到播放，播放下一首，播放上一首），播放需要设置歌曲list和具体某一歌曲索引,其他只需设置想要的播放类型
 *
 * @param
 * @author CNT on 2018/2/1.
 * @return
 * @exception
 */
public class MusicEvent {
    public final static int PLAY = 1;
    public final static int PAUSE = 2;
    public final static int STOP = 3;
    public final static int PAUSETOPLAY = 4;
    public final static int PLAYNEXT = 5;
    public final static int PLAYLAST = 6;

    private int playintStatus;
    private int position;
    private List<SongsBean> songsBeanList;

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


    public int getPlayintStatus() {
        return playintStatus;
    }

    public void setPlayintStatus(int playintStatus) {
        this.playintStatus = playintStatus;
    }


    public MusicEvent() {
    }
}
