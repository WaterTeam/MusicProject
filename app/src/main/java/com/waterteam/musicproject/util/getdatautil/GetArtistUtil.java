package com.waterteam.musicproject.util.getdatautil;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.waterteam.musicproject.bean.ArtistBean;
import com.waterteam.musicproject.bean.SongsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BA on 2017/12/8 0008.
 *
 * @Function :获取艺术家的工具
 */

public class GetArtistUtil {
    private static final String TAG = "GetArtistUtil";
    private boolean debug = false;
    //查询歌手信息
    private static final Uri externalUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

    /**
     * 获取歌手以及歌手的所有歌曲
     * //歌手id
     * MediaStore.Audio.Artists._ID
     * //歌手名
     * MediaStore.Audio.Artists.ARTIST
     * //歌手歌曲数
     * MediaStore.Audio.Artists.NUMBER_OF_TRACKS
     *
     * @param context       上下文
     * @param selection     筛选条件
     * @param selectionArgs 具体筛选值
     * @return 所有歌手的集合
     * @throws
     * @author BA on 2017/12/8 0008
     */
    public List<ArtistBean> start(Context context, String selection, String[] selectionArgs) {
        List<ArtistBean> artistList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(externalUri, null, selection, selectionArgs, MediaStore.Audio.Artists.NUMBER_OF_TRACKS + " COLLATE LOCALIZED desc");
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
                long artistId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
                List<SongsBean> songs = new GetSongUtil().start(context, MediaStore.Audio.Artists.ARTIST + "=? and "
                                + MediaStore.Audio.Media.DURATION + ">=? and "
                                + MediaStore.Audio.Media.DURATION + "<=?"
                        , new String[]{name, "90000", "1200000"});

                //我测试用的
                if (debug) {
                    Log.d(TAG, "歌手 = " + name);
                    Log.d(TAG, ("歌手ID = " + artistId));
                    Log.d(TAG, "作品数=" + songs.size());
                }

                if (songs.size() > 0)
                    artistList.add(new ArtistBean(name, artistId, songs));
            } while (cursor.moveToNext());

        }

        cursor.close();
        return artistList;
    }
}
