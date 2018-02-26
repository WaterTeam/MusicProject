package com.waterteam.musicproject.util;


import android.content.ContentUris;
import android.content.Context;

import android.graphics.Bitmap;
import android.net.Uri;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.GetCoverUri;

import java.util.concurrent.ExecutionException;


/**
 * Created by BA on 2017/12/7 0007.
 *
 * @Function : 获取专辑封面的类
 */

public class GetCoverUtil {
    private static final String TAG = "GetCoverUtil";
    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private  CompletedLoadListener loadListener;

    /**
     * 设置封面
     *
     * @param context   上下文
     * @param obj       传任意一个{@link com.waterteam.musicproject.bean.SongsBean}
     *                  {@link com.waterteam.musicproject.bean.AlbumBean}
     *                  {@link com.waterteam.musicproject.bean.ArtistBean}
     * @param imageView 显示图片的View
     * @return
     * @throws
     * @author BA on 2017/12/9 0009
     */
    public static void setCover(Context context, GetCoverUri obj, ImageView imageView) {
        loadByGlide(context, obj, imageView, 200);
    }

    /**
     * 设置加载成功回调
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/12 0012
     */
    public  GetCoverUtil setOnCompletedListener(CompletedLoadListener listener) {
        loadListener = listener;
        return this;
    }

    /**
     * 设置封面
     *
     * @param context   上下文
     * @param obj       传任意一个{@link com.waterteam.musicproject.bean.SongsBean}
     *                  {@link com.waterteam.musicproject.bean.AlbumBean}
     *                  {@link com.waterteam.musicproject.bean.ArtistBean}
     * @param imageView 显示图片的View
     * @return
     * @throws
     * @author BA on 2017/12/9 0009
     */
    public static void setCover(Context context, GetCoverUri obj, ImageView imageView, int size) {
        loadByGlide(context, obj, imageView, size);
    }

    /**
     * 随机选取封面
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/2 0002
     */
    private static void loadByGlide(Context context, GetCoverUri obj, final ImageView imageView, int size) {
        long id = obj.getAlbumId();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, id);
        int select = (int) System.currentTimeMillis() % 3;
        DrawableRequestBuilder builder = null;
        switch (select) {
            case 0:
                builder = Glide.with(context)
                        .load(uri)
                        .error(R.drawable.play_img_default);
                break;
            case 1:
                builder = Glide.with(context)
                        .load(uri)
                        .error(R.drawable.play_img_default2);
                break;
            default:
                builder = Glide.with(context)
                        .load(uri)
                        .error(R.drawable.play_img_default3);
                break;
        }

        builder.override(size, size).into(imageView);
    }


    public  void getCoverAsBitmap(Context context, GetCoverUri obj, int w,int h) {
        long id = obj.getAlbumId();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, id);
        Glide.with(context).load(uri).asBitmap().override(w,h).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (loadListener != null) {
                    loadListener.completed(resource);
                    loadListener = null;
                }
            }
        }); //方法中设置asBitmap可以设置回调类型
    }


    public interface CompletedLoadListener {
        public void completed(Bitmap bitmap);
    }
}