package com.waterteam.musicproject.bean;

import java.io.Serializable;

import com.waterteam.musicproject.viewpagers.songs.page.Cn2Spell;

import android.support.annotation.NonNull;

/**
 * Created by BA on 2017/12/7 0007.
 *
 * @Function : 用来记录歌曲信息的Bean
 */
public class SongsBean implements Serializable, GetCoverUri, Comparable<SongsBean> {
    /**
     * @author CNT on 2017/12/9.
     * <p>
     * 实现了Comparable接口，好让歌曲排序,添加了firstLetter用于排序,将耗时的firstLetter赋值写在构造函数里，这样在SplashActivity时就处理了,不然在碎片中处理会卡；
     */
    private static final String TAG = "SongsBean";
    private String location;    //储存位置
    private String name;        //名字
    private String author;      //作者
    private int length;      //长度
    private long songId;        //歌曲在系统的多媒体数据表中的Id
    private long albumId;       //专辑Id
    private String formatLenght; //用来显示的时长,一般是这样的格式：03:11

    public String getFormatLenght() {
        return formatLenght;
    }

    private String firstLetter; //歌曲名字的首字母

    public void setFirstLetter(String firstLetter){
        this.firstLetter = firstLetter;
    }
    public String getFirstLetter() {

        return firstLetter;
    }

    public long getSongId() {
        return songId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public SongsBean(String name, String author, int length, String formatLenght, long songId, long albumId, String location) {
        this.name = name;
        this.author = author;
        this.length = length;
        this.location = location;
        this.songId = songId;
        this.albumId = albumId;
        this.formatLenght = formatLenght;
    }

    public String getTime() {
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

    @Override
    public int compareTo(@NonNull SongsBean o) {
        if (getFirstLetter().equals("#") && !o.getFirstLetter().equals("#")) {
            return 1;
        } else if (!getFirstLetter().equals("#") && o.getFirstLetter().equals("#")) {
            return -1;
        }
        else if(getFirstLetter().equals("#") && o.getFirstLetter().equals("#")){
            return 1;
        }else {
            return Cn2Spell.getPinYin(getName()).compareToIgnoreCase(Cn2Spell.getPinYin(o.getName()));
        }
    }
}
