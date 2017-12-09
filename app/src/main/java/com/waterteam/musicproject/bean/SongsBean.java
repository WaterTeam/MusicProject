package com.waterteam.musicproject.bean;

import java.io.Serializable;

/**
 * Created by BA on 2017/12/7 0007.
 *
 * @Function : 用来记录歌曲信息的Bean
 */

public class SongsBean implements Serializable, GetCoverUri {
    private String location;    //储存位置
    private String name;        //名字
    private String author;      //作者
    private int length;      //长度
    private long songId;        //歌曲在系统的多媒体数据表中的Id
    private long albumId;       //专辑Id
    private String formatLenght; //用来显示的时长,一般是这样的格式：03:11

    public long getSongId() {
        return songId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public SongsBean(String name, String author, int length,String formatLenght, long songId, long albumId, String location) {
        this.name = name;
        this.author = author;
        this.length = length;
        this.location = location;
        this.songId = songId;
        this.albumId = albumId;
        this.formatLenght=formatLenght;
    }

    public String getTime(){
        return formatLenght;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return name + "::" + author + "::" + length + "::" + location;
    }
}
