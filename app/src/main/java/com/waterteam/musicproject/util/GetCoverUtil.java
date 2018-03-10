package com.waterteam.musicproject.util;


import android.content.ContentUris;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private CompletedLoadListener loadListener;
    private Bitmap defaultBitmap;

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
    public GetCoverUtil setOnCompletedListener(CompletedLoadListener listener) {
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
        DrawableRequestBuilder builder = null;
        builder = Glide.with(context)
                .load(uri)
                .error(R.drawable.play_img_default3);

        builder.override(size, size).into(imageView);
    }


    public void getCoverAsBitmap(Context context, GetCoverUri obj, int w, int h) {
        if (defaultBitmap == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outHeight = 400;
            options.outWidth = 400;
            defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.play_img_default, options);
        }
        long id = obj.getAlbumId();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, id);
        Glide.with(context).load(uri).asBitmap().override(w, h).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (loadListener != null) {
                    if (resource != null)
                        loadListener.completed(resource);
                    else
                        loadListener.completed(defaultBitmap);
                    loadListener = null;
                }
            }
        }); //方法中设置asBitmap可以设置回调类型
    }


    public interface CompletedLoadListener {
        public void completed(Bitmap bitmap);
    }
}