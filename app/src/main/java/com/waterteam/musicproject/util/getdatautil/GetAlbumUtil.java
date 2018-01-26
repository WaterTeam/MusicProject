package com.waterteam.musicproject.util.getdatautil;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.waterteam.musicproject.bean.AlbumBean;
import com.waterteam.musicproject.bean.SongsBean;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by BA on 2017/12/8 0008.
 *
 * @Function : 获取专辑的的工具类
 */

public class GetAlbumUtil {
    private static final String TAG = "GetAlbumUtil";
    private boolean debug=true;
    //查询专辑信息的Uri
    private final Uri externalUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

    /**
     * 获取专辑
     * //获取专辑id
     * MediaStore.Audio.Albums._ID
     * //获取专辑名
     * MediaStore.Audio.Albums.ALBUM
     * //获取专辑歌手
     * MediaStore.Audio.Albums.ARTIST
     * //获取专辑歌曲数
     * MediaStore.Audio.Albums.NUMBER_OF_SONGS
     *
     * @param context       上下文
     * @param selection     筛选条件
     * @param selectionArgs 具体筛选值
     * @return 所有的专辑集合
     * @throws
     * @author BA on 2017/12/8 0008
     */
    public List<AlbumBean> start(Context context, String selection, String[] selectionArgs) {
        List<AlbumBean> alubmList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(externalUri, null, selection, selectionArgs, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
                String artist=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));
                long id=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
                List<SongsBean> songs=new GetSongUtil().start(context,MediaStore.Audio.Albums.ALBUM+"=?",new String[]{name});

                if (debug){
                    Log.d(TAG, "start: ");
                    Log.d(TAG, name);
                    Log.d(TAG, artist);
                    Log.d(TAG, id+"");
                    Log.d(TAG, songs.size()+"首");
                }
               AlbumBean albumBean= new AlbumBean(name,artist,id,songs);

                alubmList.add(albumBean);
            }while (cursor.moveToNext());
        }
        return alubmList;
    }
}
