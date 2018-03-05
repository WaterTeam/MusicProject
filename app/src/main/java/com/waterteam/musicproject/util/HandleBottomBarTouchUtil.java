package com.waterteam.musicproject.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.commit451.nativestackblur.NativeStackBlur;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.adapter.AlbumSongsAdapter;
import com.waterteam.musicproject.adapter.SecondBottomAdapter;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.customview.BottomBar;
import com.waterteam.musicproject.customview.BottomBarTouchListener;
import com.waterteam.musicproject.customview.gravity_imageview.MyGravityImageView;
import com.waterteam.musicproject.customview.gravity_imageview.MySensorObserver;
import com.waterteam.musicproject.eventsforeventbus.EventFromBar;
import com.waterteam.musicproject.eventsforeventbus.EventToBarFromService;
import com.waterteam.musicproject.service.playmusic.service.PlayService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.security.AccessController.getContext;

/**
 * Created by BA on 2018/2/6 0006.
 *
 * @Function : 处理BottomBar的点击事件，注意，只发送对应的事件statu，进度条就需要传进度
 * 不用传歌曲位置
 */

public class HandleBottomBarTouchUtil implements BottomBarTouchListener {
    private static final String TAG = "HandleBottomBarTouchUti";

    private TextView bottomBar_songName;
    private TextView bottomBar_singer;
    private Button bottomBar_playButton;
    private MyGravityImageView bottomBar_image;
    private TextView bottomBar_palying_songs_name;//播放界面中的歌曲名

    private TextView bottomBar_playing_song_length;
    private TextView bottomBar_now_play_time;
    private Button bottomBar_playing_nextSong;
    private Button bottomBar_playing_lastSong;
    private Button play_mode;
    private Button up_arrow;
    private SeekBar seekBar;

//    //高斯模糊有两层，用来做动画
//    private ImageView frostedGlassImageBottom,frostedGlassImageTop;

    private ImageView frostedGlassImage;

    private MySensorObserver sensorObserver;

    private View bottomBar;
    private View bottomContent;

    private static boolean isPlaying = false;
    private static final int LISTPLAY = 0;


    @Override
    public void setContentView(final BottomBar view) {
        this.bottomBar = view.bottomBar;
        this.bottomContent = view.bottomContent;
        initGravityImageView();
        view.setVisibilityListener(new BottomBar.VisibilityListener() {
            @Override
            public void statusChange(boolean isUp) {
                if (isUp) {
                    Log.d(TAG, "statusChange: 注册");
                    sensorObserver.register(view.getContext());
                } else {
                    sensorObserver.unregister();
                    Log.d(TAG, "statusChange: 取消注册");
                }
            }
        });
        findView();
        handleClick();
        flashBottomBar();
        EventBus.getDefault().register(this);
    }

    private void initGravityImageView() {
        bottomBar_image = (MyGravityImageView) bottomContent.findViewById(R.id.play_image);

        sensorObserver = new MySensorObserver();
        sensorObserver.setMaxRotateRadian(Math.PI / 10);
        bottomBar_image.setGyroscopeObserver(sensorObserver);
    }

    private void findView() {

//        frostedGlassImageTop = (ImageView) bottomContent.findViewById(R.id.frosted_glass_image_top);
//        frostedGlassImageBottom = (ImageView) bottomContent.findViewById(R.id.frosted_glass_image_bottom);

        bottomBar_songName = (TextView) bottomBar.findViewById(R.id.bottomBar_songName);
        bottomBar_singer = (TextView) bottomBar.findViewById(R.id.bottomBar_singer);
        bottomBar_playButton = (Button) bottomBar.findViewById(R.id.bottomBar_play_button);
        bottomBar_palying_songs_name = (TextView) bottomContent.findViewById(R.id.playing_song_name);
        frostedGlassImage = (ImageView) bottomContent.findViewById(R.id.frosted_glass_image);
    }


    private void handleClick() {
        bottomBar_playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFromBar eventFromBar = new EventFromBar();
                if (isPlaying) {
                    eventFromBar.setStatu(EventFromBar.PAUSE);
                    EventBus.getDefault().post(eventFromBar);
                    isPlaying = false;
                } else {
                    eventFromBar.setStatu(EventFromBar.PAUSETOPLAY);
                    EventBus.getDefault().post(eventFromBar);
                    isPlaying = true;
                }
            }
        });
    }

    /**
     * 同步BottomBar
     * @author BA on 2018/2/26 0026
     * @param
     * @return
     * @exception
     */
    private void flashBottomBar() {
        Log.d(TAG, "flashBottomBar: ");
        SongsBean songsBean = PlayService.NowPlaySong;
        if (songsBean != null) {
            if (PlayService.isPlay) {
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
            }else{
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_play_button);
            }
            bottomBar_songName.setText(songsBean.getName());
            bottomBar_singer.setText(songsBean.getAuthor());
            bottomBar_palying_songs_name.setText(songsBean.getName());
            setCover(songsBean);
        }

    }

    @Subscribe
    public void changBottmBarView(EventToBarFromService event) {
        switch (event.getStatu()) {
            case EventToBarFromService.PAUSE: {
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_play_button);
                //seekBar要设置停住
                isPlaying = false;
            }
            break;
            case EventToBarFromService.PAUSETOPLAY: {
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                isPlaying = true;
            }
            break;
            case EventToBarFromService.PLAYANEW: {
                isPlaying = true;
                SongsBean song = event.getSongsBeanList().get(event.getPosition());
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_palying_songs_name.setText(song.getName());
                bottomBar_songName.setText(song.getName());
                bottomBar_singer.setText(song.getAuthor());
                setCover(song);
                Log.e("MainActivity", "执行一次");
            }

            break;
            default:
                break;
        }
    }

    /**
     * 变色
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/14 0014
     */
    private void setCover(SongsBean song) {
        new GetCoverUtil().setOnCompletedListener(new GetCoverUtil.CompletedLoadListener() {
            @Override
            public void completed(Bitmap bitmap) {
                Log.d(TAG, "completed: " + bitmap);
//                PaletteUtil paletteUtil = new PaletteUtil();
//                paletteUtil.from(bitmap).to(play_control_layout);
//                paletteUtil.from(bitmap).to(bottomBar_head_layout);
                //半径越大，处理后的图片越模糊
                Bitmap bm = NativeStackBlur.process(bitmap, 3);
//               startCircularReveal(frostedGlassImageTop,frostedGlassImageBottom,bm);
            }
        }).getCoverAsBitmap(bottomBar.getContext(), song, 20, 20);

        new GetCoverUtil().setOnCompletedListener(new GetCoverUtil.CompletedLoadListener() {
            @Override
            public void completed(Bitmap bitmap) {
                Log.d(TAG, "completed: " + bitmap);
//                PaletteUtil paletteUtil = new PaletteUtil();
//                paletteUtil.from(bitmap).to(play_control_layout);
//                paletteUtil.from(bitmap).to(bottomBar_head_layout);
                //半径越大，处理后的图片越模糊

                bottomBar_image.setImageBitmap(bitmap);
            }
        }).getCoverAsBitmap(bottomBar.getContext(), song, 400, 400);
    }

    /**
     * 设置揭露动画
     * @author BA on 2018/2/28 0028
     * @param top 顶部用来实现揭露动画的View
     * @param bottom 底部用来默认显示的View
     * @param bm 要刷新的图片
     * @return
     * @exception
     */
    public void startCircularReveal(final ImageView top, final ImageView bottom, final Bitmap bm) {
        top.setWillNotDraw(false);
        top.setImageBitmap(bm);
        int finalRadius = Math.max(top.getWidth(), top.getHeight());
        Animator anim =
                ViewAnimationUtils.createCircularReveal(top, top.getWidth(), top.getHeight() / 2, 0, finalRadius + 300);

        anim.setDuration(800);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                bottom.setImageBitmap(bm);
                top.setVisibility(View.INVISIBLE);
                top.setWillNotDraw(true);
            }
        });
        top.setVisibility(View.VISIBLE);
        anim.start();
    }
}