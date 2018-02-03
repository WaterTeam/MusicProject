package com.waterteam.musicproject.bean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BA on 2017/12/7 0007.
 *
 * @Function : 用来记录唱片的Bean
 */

public class AlbumBean implements Serializable,GetCoverUri{
    //该专辑下的歌曲
    private List<SongsBean> songs;
    //专辑名
    private String name;
    //专辑Id
    private long album_id;
    //歌手
    private String artist;

    public AlbumBean(String name, String artist, long album_id, List<SongsBean> songs){
        this.name=name;
        this.artist=artist;
        this.album_id=album_id;
        this.songs=songs;
    }

    public String getArtist() {
        return artist;
    }

    public List<SongsBean> getSongs() {
        return songs;
    }

    public int getSongsCount(){return songs.size();}

    public String getName() {
        return name;
    }

    public long getAlbumId() {
        return album_id;
    }
}
