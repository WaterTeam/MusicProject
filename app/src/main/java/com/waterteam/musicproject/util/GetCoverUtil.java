package com.waterteam.musicproject.util;


import android.content.ContentUris;
import android.content.Context;

import android.net.Uri;

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
        long id=obj.getAlbumId();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, id);
        Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.play_img_default)
                .error(R.drawable.play_img_default)
                .override(200, 200)
                .into(imageView);
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
        long id=obj.getAlbumId();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, id);
        Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.play_img_default)
                .error(R.drawable.play_img_default)
                .override(size, size)
                .into(imageView);
    }
}