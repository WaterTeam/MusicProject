package com.waterteam.musicproject.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.waterteam.musicproject.bean.SongsBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BA on 2017/12/7 0007.
 *
 * @Function : 用来将从手机上查到的歌曲信息映射成对象的工具类
 */
public class GetSongUtil {
    //是否进行测试
    private boolean debug=true;
    //用来查表的Uri
    private final Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static final String TAG = "GetSongUtil";

    /**
     * 获取所有歌曲信息，其中查询音乐信息的字段是
     * 歌曲的名称 ：MediaStore.Audio.Media.TITLE
     * 歌曲的专辑名：MediaStore.Audio.Media.ALBUM
     * 歌曲的歌手名： MediaStore.Audio.Media.ARTIST
     * 歌曲文件的全路径 ：MediaStore.Audio.Media.DATA
     * 歌曲文件的名称：MediaStroe.Audio.Media.DISPLAY_NAME
     * 歌曲文件的发行日期：MediaStore.Audio.Media.YEAR
     * 歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
     * 歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
     *
     * @param context 上下文
     * @param selection 筛选条件
     * @param selectionArgs 具体筛选值
     * @author BA on 2017/12/7 0007
     */
    public List<SongsBean> start(Context context,String selection,String[] selectionArgs) {
        List<SongsBean> songsList=new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(externalUri, null, selection, selectionArgs, null);
        if (cursor.moveToFirst()) {
            do {
                String name=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String location=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String artis=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String length=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                long songId=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                long albumId=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                //我测试用的
                if (debug) {
                    Log.d(TAG,("路径 = " + location));
                    Log.d(TAG,("歌手 = " + artis));
                    Log.d(TAG,("时长 = " + length));
                    Log.d(TAG,"歌名 = "+name);
                    Log.d(TAG,("id="+songId));
                }

                songsList.add(new SongsBean(name,artis,length,songId,albumId,location));
            } while (cursor.moveToNext());

        }

        cursor.close();
        Log.d(TAG, "歌曲数目="+songsList.size());
        return songsList;
    }
}
