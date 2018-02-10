package com.waterteam.musicproject.eventsforeventbus;

import com.waterteam.musicproject.bean.SongsBean;

import java.util.List;

/**
 * Created by BA on 2018/2/6 0006.
 *
 * @Function : 歌曲点击时，长按，用这个传递事件
 */

public class EventFromTouch {
    //单击
    public final static int NOW_PLAY=2; //马上播放，这是点击歌曲后的

    //长按歌曲
    public final static int ADD_TO_NEXT=1; //添加到下一首播放
    public final static int ADD_TO_LIST=3;
    public final static int DELETE_FROM_LIST=4;
    public final static int ALWAYS_PLAY=5; //单曲循环

    //直接传一个歌曲过来
    private SongsBean songsBean;

    //点击播放全部按钮
    public final static int ADD_ALL_TO_LIST=5;

    //传一个歌曲lis过来
    public List<SongsBean> songs;

    //区别事件类型
    private int statu;

    //点击的歌在列表中的索引
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }



    public void setStatu(int statu){
        this.statu=statu;
    }

    public int getStatu(){
        return statu;
    }

    public void setSong(SongsBean song){
        this.songsBean=song;
    }

    public SongsBean getSong(){return songsBean;}

    public void setSongs(List<SongsBean> songs){
        this.songs=songs;
    }

    public List<SongsBean> getSongs(){
        return songs;
    }
}
