package com.waterteam.musicproject.bean;

/**
 * Created by BA on 2017/12/7 0007.
 *
 * @Function : 用来记录歌曲信息的Bean
 */

public class SongsBean {
    private String location;    //储存位置
    private String name;        //名字
    private String author;      //作者
    private String length;      //长度
    private long songId;        //歌曲在系统的多媒体数据表中的Id
    private long albumId;       //专辑Id

    public long getSongId() {
        return songId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public SongsBean(String name, String author, String length,long songId,long albumId, String location){
        this.name=name;
        this.author=author;
        this.length=length;
        this.location=location;
        this.songId=songId;
        this.albumId=albumId;
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

    public String getLength() {
        return length;
    }

    @Override
    public String toString() {
        return name+"::"+author+"::"+length+"::"+location;
    }
}
