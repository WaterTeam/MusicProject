package com.waterteam.musicproject.util;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.eventsforeventbus.EventFromBar;
import com.waterteam.musicproject.eventsforeventbus.EventToBarFromService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by BA on 2018/2/6 0006.
 *
 * @Function : 处理BottomBar的点击事件，注意，只发送对应的事件statu，进度条就需要传进度
 * 不用传歌曲位置
 */

public class HandleBottomBarTouchUtil {

    public List<SongsBean> songsBeanList;

    private TextView bottomBar_songName;
    private TextView bottomBar_singer;
    private Button bottomBar_playButton;
    private ImageView bottomBar_image;
    private Button bottomBar_playingLayout_button;//播放界面中的播放按钮
    private TextView bottomBar_palying_songs_name;//播放界面中的歌曲名
    private TextView bottomBar_playing_song_length;
    private Button bottomBar_playing_nextSong;
    private Button bottomBar_playing_lastSong;
    private SeekBar seekBar;

    private View bottomBar;
    private View bottomContent;

    private static boolean isPlaying = false;

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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                try {
//                    iMyMusicService.seekPlayProgress(seekBar.getProgress());
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
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
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                //seekBar设置恢复移动
            }
            break;
            case EventToBarFromService.PLAYANEW: {
                isPlaying = true;
                SongsBean song = event.getSongsBeanList().get(event.getPosition());
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_palying_songs_name.setText(song.getName());
                bottomBar_songName.setText(song.getName());
                bottomBar_singer.setText(song.getAuthor());
                bottomBar_playing_song_length.setText(song.getFormatLenght());
                GetCoverUtil.setCover(bottomBar.getContext(), song, bottomBar_image, 600);

                //seekBar.setMax(playingBarEvent.getSongsBeanList().get(playingBarEvent.getPosition()));
            }

            break;
            case EventToBarFromService.MOVESEEKBAR:{

            }
            break;
            default:
                break;
        }
    }
}
