package com.waterteam.musicproject.service.playmusic.service;

/**
 * Created by Administrator on 2018/2/1.
 */

/**
 *用于EventBus的一个事件类，使用时设置歌曲的URL，设置想要的播放情况（播放，暂停，停止），暂停和停止不需要设置歌曲URL
 *
 * @author CNT on 2018/2/1.
 * @param
 * @return
 * @exception
 */
public class MusicEvent {
    public final static int PLAY = 1;
    public final static int PAUSE = 2;
    public final static int STOP = 3;

    String songURL;
    int playintStatus;


    public int getPlayintStatus() {
        return playintStatus;
    }

    public void setPlayintStatus(int playintStatus) {
        this.playintStatus = playintStatus;
    }



    public String getSongURL() {
        return songURL;
    }

    public void setSongURL(String songURL) {
        this.songURL = songURL;
    }


    public MusicEvent() {
        songURL = "";
        playintStatus = 0;
    }
}
