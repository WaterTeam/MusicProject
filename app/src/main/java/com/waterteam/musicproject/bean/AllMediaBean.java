package com.waterteam.musicproject.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BA on 2017/12/8 0008.
 *
 * @Function : 全局存储歌曲信息，谷歌推荐这样写
 */

public class AllMediaBean implements Serializable {
    private List<ArtistBean> artists;
    private List<SongsBean> songs;

    public List<ArtistBean> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistBean> artists) {
        this.artists = artists;
    }

    public List<SongsBean> getSongs() {
        return songs;
    }

    public void setSongs(List<SongsBean> songs) {
        this.songs = songs;
    }

    private AllMediaBean() {
    }

    public static AllMediaBean getInstance() {
        return SingleInstance.MY_SONGS_DATA;
    }

    private static class SingleInstance {
        private static final AllMediaBean MY_SONGS_DATA = new AllMediaBean();
    }
}
