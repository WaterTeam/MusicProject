package com.waterteam.musicproject.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.waterteam.musicproject.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by BA on 2017/12/7 0007.
 *
 * @Function : 获取专辑封面的类,该类待测试
 */

public class GetSongsCoverUtil {
    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    //做缓存，有可能连着几首歌都是一个专辑的，那直接返回上一次的专辑封面就好
    private static Bitmap cacheBm;
    private static long cacheAlbum_id;

    /**
     * 获取专辑封面，一般是通过album_id获取的
     * @author BA on 2017/12/8 0008
     * @param context 上下文
     * @param song_id 用来获取封面的
     * @param album_id 同上，这两个参数放在了{@link com.waterteam.musicproject.bean.SongsBean}
     * @return 封面
     * @exception IOException
     * @exception  FileNotFoundException
     */
    public static Bitmap getArtwork(Context context, long song_id, long album_id,
                             boolean allowDefault) {
        if(cacheAlbum_id==album_id){
            return cacheBm;
        }
        if (album_id < 0) {
            // This is something that is not in the database, so get the album art directly
            // from the file.
            if (song_id >= 0) {
               return getArtworkFromFile(context, song_id, -1);
            }
            if (allowDefault) {
                return getDefaultArtwork(context);
            }
            return null;
        }

        cacheAlbum_id=album_id;
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, sBitmapOptions);
            } catch (FileNotFoundException ex) {
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or
                // maybe it never existed to begin with.
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);
                if (bm != null) {
                    if (bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if (bm == null && allowDefault) {
                            return getDefaultArtwork(context);
                        }
                    }
                } else if (allowDefault) {
                    bm = getDefaultArtwork(context);
                }
                return bm;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 从文件中获取歌曲封面
     * @author BA on 2017/12/8 0008
     * @param context {@link GetSongsCoverUtil#getArtwork(Context, long, long, boolean)}
     * @return 同上
     * @exception FileNotFoundException
     */
    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid) {
        Bitmap bm = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        if (bm==null){
            return getDefaultArtwork(context);
        }
        cacheBm=bm;
        return bm;
    }

    /**
     *  当歌曲中没有分封面的时候，就设置个默认的封面，默认封面随便找了张图，因为我还没画
     * @author BA on 2017/12/8 0008
     * @param
     * @return  drawable/paly_img_default
     * @exception
     */
    private static Bitmap getDefaultArtwork(Context context) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bm=BitmapFactory.decodeResource(context.getResources(), R.drawable.play_img_default, opts);
        cacheBm=bm;
        return bm;
    }
}
