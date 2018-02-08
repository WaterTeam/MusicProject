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


    private List<SongsBean> songs = new ArrayList<SongsBean>();

    public List<SongsBean> getSongs() {
        return songs;
    }

    public void addSongsToFoot(SongsBean songsBean) {
//        int index=songs.indexOf(songsBean);
//        if (index==-1) {
//            songs.add(songsBean);
//            return true;
//        }
//        return false;
        songs.add(songsBean);
    }

    public SongsBean getSong(int position) {
        return songs.get(position);
    }

    public void removeSong(SongsBean songsBean) {
        songs.remove(songsBean);
    }

    public void addSongToPosition(int position, SongsBean songsBean) {
//        int index=songs.indexOf(songsBean);
//        if (index==-1) {
//            songs.add(position,songsBean);
//            return true;
//        }
//        return false;
        songs.add(position, songsBean);
    }

    public void removeAll() {
        songs.clear();
    }

    public void addList(List<SongsBean> songs) {
        if (songs != null) {
            this.songs.clear();
            this.songs.addAll(songs);
        }
    }

    public int getSongsCount() {
        return songs.size();
    }
}
