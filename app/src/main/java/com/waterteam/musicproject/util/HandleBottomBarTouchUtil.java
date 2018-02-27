package com.waterteam.musicproject.util;

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
    private Button bottomBar_playingLayout_button;//播放界面中的播放按钮
    private TextView bottomBar_palying_songs_name;//播放界面中的歌曲名
    private TextView bottomBar_playing_song_length;
    private TextView bottomBar_now_play_time;
    private Button bottomBar_playing_nextSong;
    private Button bottomBar_playing_lastSong;
    private Button play_mode;
    private Button up_arrow;
    private SeekBar seekBar;
    private ImageView frostedGlassImage;
    private MySensorObserver sensorObserver;

    private View bottomBar;
    private View bottomContent;

    private BottomBar bottomBarSecond;
    private BottomBar bottomBarFirst;
    private RecyclerView recyclerView;
    private TextView playlistCount;

    private static boolean isPlaying = false;
    private static final int LISTPLAY = 0;
    private static final int ALWAYSPLAY = 1;
    private static final int RANDOMPLAY = 2;
    private static int playMode = LISTPLAY;


    @Override
    public void setContentView(final BottomBar view) {
        bottomBarFirst = view;
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
        frostedGlassImage = (ImageView) bottomContent.findViewById(R.id.frosted_glass_image);
        bottomBar_songName = (TextView) bottomBar.findViewById(R.id.bottomBar_songName);
        bottomBar_singer = (TextView) bottomBar.findViewById(R.id.bottomBar_singer);
        bottomBar_playButton = (Button) bottomBar.findViewById(R.id.bottomBar_play_button);
        bottomBar_playingLayout_button = (Button) bottomContent.findViewById(R.id.play_or_pause_button);
        bottomBar_palying_songs_name = (TextView) bottomContent.findViewById(R.id.playing_song_name);
        bottomBar_playing_song_length = (TextView) bottomContent.findViewById(R.id.playing_song_length);
        bottomBar_playing_nextSong = (Button) bottomContent.findViewById(R.id.next_song_button);
        bottomBar_playing_lastSong = (Button) bottomContent.findViewById(R.id.last_song_button);
        bottomBar_now_play_time = (TextView) bottomContent.findViewById(R.id.play_progress);
        play_mode = (Button) bottomContent.findViewById(R.id.play_mode);
        seekBar = (SeekBar) bottomContent.findViewById(R.id.seekbar);
        up_arrow = (Button)bottomContent.findViewById(R.id.up_arrow);
        bottomBarSecond = (BottomBar) bottomContent.findViewById(R.id.second_bottomBar);
        bottomBarSecond.isSecond = true;
        recyclerView = (RecyclerView) bottomBarSecond.findViewById(R.id.second_bottomBar_recycleView);
        playlistCount = (TextView) bottomBarSecond.findViewById(R.id.play_list_count);
    }


    private void handleClick() {
        bottomBar_playingLayout_button.setOnClickListener(new View.OnClickListener() {
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
        bottomBar_playing_nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFromBar eventFromBar = new EventFromBar();
                eventFromBar.setStatu(EventFromBar.PLAYNEXT);
                EventBus.getDefault().post(eventFromBar);
                isPlaying = true;
            }
        });
        bottomBar_playing_lastSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFromBar eventFromBar = new EventFromBar();
                eventFromBar.setStatu(EventFromBar.PLAYLAST);
                EventBus.getDefault().post(eventFromBar);
                isPlaying = true;
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bottomBar_now_play_time.setText(formatTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EventFromBar eventFromBar = new EventFromBar();
                eventFromBar.setStatu(EventFromBar.SEEKBARMOVE);
                eventFromBar.setProgress(seekBar.getProgress());
                EventBus.getDefault().post(eventFromBar);
            }
        });
        play_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFromBar eventFromBar = new EventFromBar();
                playMode = (playMode + 1) % 3;
                switch (playMode) {
                    case LISTPLAY:
                        eventFromBar.setStatu(EventFromBar.LISTMODE);
                        EventBus.getDefault().post(eventFromBar);
                        play_mode.setBackgroundResource(R.drawable.ic_list_play_button);
                        Toast toast = Toast.makeText(bottomBar.getContext(), "列表循环", Toast.LENGTH_LONG);
                        showMyToast(toast, 500);
                        break;
                    case ALWAYSPLAY:
                        eventFromBar.setStatu(EventFromBar.SIMPLEMODE);
                        EventBus.getDefault().post(eventFromBar);
                        play_mode.setBackgroundResource(R.drawable.ic_single_play_button);
                        Toast toast1 = Toast.makeText(bottomBar.getContext(), "单曲循环", Toast.LENGTH_LONG);
                        showMyToast(toast1, 500);
                        break;
                    case RANDOMPLAY:
                        eventFromBar.setStatu(EventFromBar.RANDOMMODE);
                        EventBus.getDefault().post(eventFromBar);
                        play_mode.setBackgroundResource(R.drawable.ic_random_play_button);
                        Toast toast2 = Toast.makeText(bottomBar.getContext(), "随机播放", Toast.LENGTH_LONG);
                        showMyToast(toast2, 500);
                        break;
                    default:
                        break;
                }
            }
        });
        up_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bottomBarSecond.getIsPullUp()){
                    bottomBarSecond.pullUp();
                }
                else{
                    bottomBarSecond.pullDown();//第二个bottomBar下拉后不应该去影响状态栏，状态栏由第一个bottomBar决定
                    StatusBarUtil.setStatusBarDarkMode((Activity)bottomBarSecond.getContext());
                    bottomBarFirst.setVisilityChange(false);
                }
            }
        });
    }

    private void flashBottomBar() {
        Log.d(TAG, "flashBottomBar: ");
        SongsBean songsBean = PlayService.NowPlaySong;
        if (songsBean != null) {
            if (PlayService.isPlay) {
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_pause_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
            }
            bottomBar_songName.setText(songsBean.getName());
            bottomBar_singer.setText(songsBean.getAuthor());
            bottomBar_palying_songs_name.setText(songsBean.getName());
            bottomBar_playing_song_length.setText(songsBean.getFormatLenght());

            setCover(songsBean);

            seekBar.setMax(songsBean.getLength());

            switch (PlayService.playMode) {
                case EventFromBar.LISTMODE:
                    play_mode.setBackgroundResource(R.drawable.ic_list_play_button);
                    break;
                case EventFromBar.SIMPLEMODE:
                    play_mode.setBackgroundResource(R.drawable.ic_single_play_button);
                    break;
                case EventFromBar.RANDOMMODE:
                    play_mode.setBackgroundResource(R.drawable.ic_random_play_button);
                    break;
                default:
                    break;
            }
            SecondBottomAdapter secondBottomAdapter = new SecondBottomAdapter(PlayService.playList.getSongs());
            recyclerView.setAdapter(secondBottomAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(bottomBarFirst.getContext());
            layoutManager.scrollToPositionWithOffset(PlayService.position, 0);
            //layoutManager.setStackFrom(true);
            recyclerView.setLayoutManager(layoutManager);
            playlistCount.setText("  播放列表（"+PlayService.playList.getSongs().size()+"）");
        }

    }

    @Subscribe
    public void changBottomBarView(EventToBarFromService event) {
        switch (event.getStatu()) {
            case EventToBarFromService.PAUSE: {
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_play_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_play_button);
                //seekBar要设置停住
                isPlaying = false;
            }
            break;
            case EventToBarFromService.PAUSETOPLAY: {
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_pause_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                isPlaying = true;
            }
            break;
            case EventToBarFromService.PLAYANEW: {
                if (event.getPlayMode() != -1) {
                    switch (event.getPlayMode()) {
                        case LISTPLAY:
                            play_mode.setBackgroundResource(R.drawable.ic_list_play_button);
                            break;
                        case ALWAYSPLAY:
                            play_mode.setBackgroundResource(R.drawable.ic_single_play_button);
                            break;
                        case RANDOMPLAY:
                            play_mode.setBackgroundResource(R.drawable.ic_random_play_button);
                            break;
                        default:
                            break;
                    }
                    playMode = event.getPlayMode();
                }
                isPlaying = true;
                SongsBean song = event.getSongsBeanList().get(event.getPosition());
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_pause_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_palying_songs_name.setText(song.getName());
                bottomBar_songName.setText(song.getName());
                bottomBar_singer.setText(song.getAuthor());
                bottomBar_playing_song_length.setText(song.getFormatLenght());
                setCover(song);
                seekBar.setProgress(0);
                seekBar.setMax(song.getLength());

                LinearLayoutManager layoutManager = new LinearLayoutManager(bottomBarFirst.getContext());
                layoutManager.scrollToPositionWithOffset(PlayService.position, 0);
                layoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(layoutManager);
                playlistCount.setText("  播放列表（"+PlayService.playList.getSongs().size()+"）");
                Log.e("MainActivity", "执行一次");
            }

            break;
            case EventToBarFromService.MOVESEEKBAR: {
                bottomBar_now_play_time.setText(formatTime(event.getProgress()));
                seekBar.setProgress(event.getProgress());
            }
            break;
            case EventToBarFromService.SEEKBARMOVEITSELF: {
                bottomBar_now_play_time.setText(formatTime(event.getProgress()));
                seekBar.setMax(event.getSongsBeanList().get(event.getPosition()).getLength());
                seekBar.setProgress(event.getProgress());
            }
            break;
            default:
                break;
        }
    }

    private String formatTime(int Leng) {
        //格式化时长
        float fLength = Leng / 1000.00f / 60.00f;
        fLength = (float) Math.round(fLength * 100);
        fLength = fLength / 100;
        String sLength = fLength + "";

        String[] time = (sLength).split("\\.");
        String min = "";
        String second = "";
        if (time.length >= 1) min = time[0];
        if (time.length >= 2) second = time[1];
        if (second.length() < 2) {
            second += "0";
        }
        return min + ":" + second;
    }

    /**
     * 自定义Toast的显示时间
     *
     * @param
     * @return
     * @throws
     * @author CNT on 2018/2/10.
     */
    public void showMyToast(final Toast toast, final int cnt) {//设置了Toast显示的时间
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3500);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);
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