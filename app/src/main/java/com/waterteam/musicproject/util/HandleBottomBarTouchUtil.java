package com.waterteam.musicproject.util;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.customview.BottomBar;
import com.waterteam.musicproject.customview.BottomBarTouchListener;
import com.waterteam.musicproject.customview.gravity_imageview.MySensorObserver;
import com.waterteam.musicproject.customview.gravity_imageview.RotationCarView;
import com.waterteam.musicproject.eventsforeventbus.EventFromBar;
import com.waterteam.musicproject.eventsforeventbus.EventToBarFromService;
import com.waterteam.musicproject.service.playmusic.service.PlayService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by BA on 2018/2/6 0006.
 *
 * @Function : 处理BottomBar的点击事件，注意，只发送对应的事件statu，进度条就需要传进度
 * 不用传歌曲位置
 */

public class HandleBottomBarTouchUtil implements BottomBarTouchListener {
    private static final String TAG = "HandleBottomBarTouchUti";

    private RotationCarView view;

    private TextView bottomBar_songName;
    private TextView bottomBar_singer;
    private Button bottomBar_playButton;
    private ImageView bottomBar_image;
    private TextView bottomBar_palying_songs_name;//播放界面中的歌曲名
    

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
        bottomBar_image = (ImageView) bottomContent.findViewById(R.id.play_image);

        view=(RotationCarView)bottomContent.findViewById(R.id.rcv);

        sensorObserver = new MySensorObserver();
        sensorObserver.setMaxRotateRadian(Math.PI / 10);

        view.setGyroscopeObserver(sensorObserver);

    }

    private void findView() {
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
                frostedGlassImage.setImageBitmap(bm);
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
}