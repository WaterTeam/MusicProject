package viewpagers.songs.page;

import android.support.annotation.NonNull;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Created by Administrator on 2017/12/7.
 */

public class SongsRV_Data implements Comparable<SongsRV_Data>{

    private int id;                 //定位歌曲具体位置,便于数据库查询
    private String songName;
    private String singer;
    private String songTime;
    private String pinYin;          //歌曲拼音
    private String firstLetter;     //歌曲拼音第一个字母

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSongTime() {
        return songTime;
    }

    public void setSongTime(String songTime) {
        this.songTime = songTime;
    }

    public String getPinYin() {
        return Cn2Spell.getPinYin(songName);
    }

    public String getFirstLetter() {
        firstLetter = getPinYin().substring(0,1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
        return firstLetter;
    }

    //实现Comparable接口，用于给集合中的数据排序
    @Override
    public int compareTo(@NonNull SongsRV_Data o) {
        if (getFirstLetter().equals("#") && !o.getFirstLetter().equals("#")) {
            return 1;
        } else if (!getFirstLetter().equals("#") && o.getFirstLetter().equals("#")){
            return -1;
        } else {
            return getPinYin().compareToIgnoreCase(o.getPinYin());
        }
    }
}
