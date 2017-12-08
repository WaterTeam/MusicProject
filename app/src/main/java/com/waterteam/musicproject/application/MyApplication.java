package com.waterteam.musicproject.application;

import android.app.Application;

import com.waterteam.musicproject.bean.AlbumBean;
import com.waterteam.musicproject.bean.ArtistBean;
import com.waterteam.musicproject.bean.SongsBean;

import java.util.List;

/**
 * Created by BA on 2017/12/8 0008.
 *
 * @Function : 全局存储歌曲信息，其实有没有这个必要还不知道，暂时现这样写
 */

public class MyApplication extends Application {
    private List<ArtistBean> artists;
    private List<AlbumBean> albums;
    private List<SongsBean> songs;

    public List<ArtistBean> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistBean> artists) {
        this.artists = artists;
    }

    public List<AlbumBean> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumBean> albums) {
        this.albums = albums;
    }

    public List<SongsBean> getSongs() {
        return songs;
    }

    public void setSongs(List<SongsBean> songs) {
        this.songs = songs;
    }
}
