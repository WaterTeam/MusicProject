package com.waterteam.musicproject.util;


import android.content.ContentUris;
import android.content.Context;

import android.net.Uri;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.GetCoverUri;


/**
 * Created by BA on 2017/12/7 0007.
 *
 * @Function : 获取专辑封面的类
 */

public class GetCoverUtil {
    private static final String TAG = "GetCoverUtil";
    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

    /**
     * 设置封面
     *
     * @param context 上下文
     * @param obj  传任意一个{@link com.waterteam.musicproject.bean.SongsBean}
     *             {@link com.waterteam.musicproject.bean.AlbumBean}
     *             {@link com.waterteam.musicproject.bean.ArtistBean}
     * @param imageView 显示图片的View
     * @return
     * @throws
     * @author BA on 2017/12/9 0009
     */
    public static void setCover(Context context, GetCoverUri obj, ImageView imageView) {
        loadByGlide(context,obj,imageView,200);
    }

    /**
     * 设置封面
     *
     * @param context 上下文
     * @param obj  传任意一个{@link com.waterteam.musicproject.bean.SongsBean}
     *             {@link com.waterteam.musicproject.bean.AlbumBean}
     *             {@link com.waterteam.musicproject.bean.ArtistBean}
     * @param imageView 显示图片的View
     * @return
     * @throws
     * @author BA on 2017/12/9 0009
     */
    public static void setCover(Context context, GetCoverUri obj, ImageView imageView,int size) {
        loadByGlide(context,obj,imageView,size);
    }

    /**
     * 随机选取封面
     * @author BA on 2018/2/2 0002
     * @param
     * @return
     * @exception
     */
    private static void loadByGlide(Context context, GetCoverUri obj, ImageView imageView,int size){
        long id=obj.getAlbumId();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, id);
        int select=(int)System.currentTimeMillis()%3;
        switch (select){
            case 0:
                Glide.with(context)
                        .load(uri)
                        .error(R.drawable.play_img_default)
                        .override(size, size)
                        .into(imageView);
                break;
            case 1:
                Glide.with(context)
                        .load(uri)
                        .error(R.drawable.play_img_default2)
                        .override(size, size)
                        .into(imageView);
                break;
            default:
                Glide.with(context)
                        .load(uri)
                        .error(R.drawable.play_img_default3)
                        .override(size, size)
                        .into(imageView);
                break;
        }
    }
}