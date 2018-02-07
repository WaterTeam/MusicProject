package com.waterteam.musicproject.bean;

import com.waterteam.musicproject.bean.SongsBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BA on 2018/2/6 0006.
 *
 * @Function : 正在播放列表
 */

public class WaitingPlaySongs implements Serializable {
    private List<SongsBean> songs=new ArrayList<>();

    public boolean addSongsToFoot(SongsBean songsBean){
        int index=songs.indexOf(songsBean);
        if (index==-1) {
            songs.add(songsBean);
            return true;
        }
        return false;
    }

    public SongsBean getSong(int position){
        return songs.get(position);
    }

    public void removeSong(SongsBean songsBean){
        songs.remove(songsBean);
    }

    public boolean addSongToPosition(int position,SongsBean songsBean){
        int index=songs.indexOf(songsBean);
        if (index==-1) {
            songs.add(position,songsBean);
            return true;
        }
        return false;
    }

    public void removeAll(){
        songs.clear();
    }

    public void addList(List<SongsBean> songs){
        this.songs.clear();
        this.songs.addAll(songs);
    }

    public int getSongsCount(){return songs.size();}
}
