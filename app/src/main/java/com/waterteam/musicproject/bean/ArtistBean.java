package com.waterteam.musicproject.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BA on 2017/12/7 0007.
 *
 * @Function : 用来记录艺术家的Bean
 */

public class ArtistBean implements Serializable, GetCoverUri {
    private String name;
    private long id;
    private List<SongsBean> songs;
    private List<AlbumBean> albums;

    public List<AlbumBean> getAlubums(){
        return albums;
    }

    public ArtistBean(String name, long id,List<AlbumBean> albums, List<SongsBean> songs) {
        this.name = name;
        this.id = id;
        this.songs = songs;
        this.albums=albums;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public List<SongsBean> getSongs() {
        return songs;
    }

    public int getSongsCount() {
        return songs.size();
    }

    public int getAlbumCount(){return albums.size();}

    @Override
    public long getAlbumId() {
        return songs.get(0).getAlbumId();
    }
}
