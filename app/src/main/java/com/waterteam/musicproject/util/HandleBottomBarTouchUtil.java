package com.waterteam.musicproject.util;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.eventsforeventbus.EventFromBar;
import com.waterteam.musicproject.eventsforeventbus.EventToBarFromService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by BA on 2018/2/6 0006.
 *
 * @Function : 处理BottomBar的点击事件，注意，只发送对应的事件statu，进度条就需要传进度
 * 不用传歌曲位置
 */

public class HandleBottomBarTouchUtil {

    private TextView bottomBar_songName;
    private TextView bottomBar_singer;
    private Button bottomBar_playButton;
    private ImageView bottomBar_image;
    private Button bottomBar_playingLayout_button;//播放界面中的播放按钮
    private TextView bottomBar_palying_songs_name;//播放界面中的歌曲名
    private TextView bottomBar_playing_song_length;
    private TextView bottomBar_now_play_time;
    private Button bottomBar_playing_nextSong;
    private Button bottomBar_playing_lastSong;
    private Button play_mode;
    private SeekBar seekBar;

    private View bottomBar;
    private View bottomContent;

    private static boolean isPlaying = false;
    private static int playMode = 0;

    public HandleBottomBarTouchUtil(View bottomBar, View bottomContent) {
        this.bottomBar = bottomBar;
        this.bottomContent = bottomContent;

        findView();
        handleClick();
        EventBus.getDefault().register(this);
    }

    private void findView() {
        bottomBar_songName = (TextView) bottomBar.findViewById(R.id.bottomBar_songName);
        bottomBar_singer = (TextView) bottomBar.findViewById(R.id.bottomBar_singer);
        bottomBar_playButton = (Button) bottomBar.findViewById(R.id.bottomBar_play_button);
        bottomBar_image = (ImageView) bottomContent.findViewById(R.id.play_image);
        bottomBar_playingLayout_button = (Button) bottomContent.findViewById(R.id.play_button);
        bottomBar_palying_songs_name = (TextView) bottomContent.findViewById(R.id.palying_songs_name);
        bottomBar_playing_song_length = (TextView) bottomContent.findViewById(R.id.palying_song_length);
        bottomBar_playing_nextSong = (Button) bottomContent.findViewById(R.id.nextsong_button);
        bottomBar_playing_lastSong = (Button) bottomContent.findViewById(R.id.lastsong_button);
        bottomBar_now_play_time = (TextView) bottomContent.findViewById(R.id.paly_progress);
        play_mode = (Button) bottomContent.findViewById(R.id.play_mode);
        seekBar = (SeekBar) bottomContent.findViewById(R.id.seekbar);
    }

    public void handleClick() {
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
                    case 0:
                        eventFromBar.setStatu(EventFromBar.LISTMODE);
                        EventBus.getDefault().post(eventFromBar);
                        play_mode.setBackgroundResource(R.drawable.ic_liebiao);
                        Toast toast = Toast.makeText(bottomBar.getContext(),"列表循环",Toast.LENGTH_LONG);
                        showMyToast(toast,500);
                        break;
                    case 1:
                        eventFromBar.setStatu(EventFromBar.SIMPLEMODE);
                        EventBus.getDefault().post(eventFromBar);
                        play_mode.setBackgroundResource(R.drawable.ic_danqu);
                        Toast toast1 = Toast.makeText(bottomBar.getContext(),"单曲循环",Toast.LENGTH_LONG);
                        showMyToast(toast1,500);
                        break;
                    case 2:
                        eventFromBar.setStatu(EventFromBar.RANDOMMODE);
                        EventBus.getDefault().post(eventFromBar);
                        play_mode.setBackgroundResource(R.drawable.ic_suiji);
                        Toast toast2 = Toast.makeText(bottomBar.getContext(),"随机播放",Toast.LENGTH_LONG);
                        showMyToast(toast2,500);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Subscribe
    public void changBottomBarView(EventToBarFromService event) {
        switch (event.getStatu()) {
            case EventToBarFromService.PAUSE: {
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_play_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_play_button);
                //seekBar要设置停住
            }
            break;
            case EventToBarFromService.PAUSETOPLAY: {
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_pause_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
            }
            break;
            case EventToBarFromService.PLAYANEW: {
                isPlaying = true;
                SongsBean song = event.getSongsBeanList().get(event.getPosition());
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_pause_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_palying_songs_name.setText(song.getName());
                bottomBar_songName.setText(song.getName());
                bottomBar_singer.setText(song.getAuthor());
                bottomBar_playing_song_length.setText(song.getFormatLenght());
                GetCoverUtil.setCover(bottomBar.getContext(), song, bottomBar_image, 200);
                seekBar.setProgress(0);
                seekBar.setMax(song.getLength());

            }

            break;
            case EventToBarFromService.MOVESEEKBAR: {
                bottomBar_now_play_time.setText(formatTime(event.getProgress()));
                seekBar.setProgress(event.getProgress());
            }
            break;
            case EventToBarFromService.SEEKBARMOVEITSELF: {
                bottomBar_now_play_time.setText(formatTime(event.getProgress()));
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
     * @author CNT on 2018/2/10.
     * @param
     * @return
     * @exception
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
        }, cnt );
    }

}