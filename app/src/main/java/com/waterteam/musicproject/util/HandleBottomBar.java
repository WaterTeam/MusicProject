package com.waterteam.musicproject.util;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.eventsforeventbus.MusicEvent;
import com.waterteam.musicproject.eventsforeventbus.PlayingBarEvent;

import org.greenrobot.eventbus.EventBus;


/**
 * 用于设置Mainactivity中的BoottomBar点击事件,设置点击事件为new HandleBottomBar(mainActivity).handleClick();
 * <p>
 * 如果在activity中要设置bottomBar的界面改变，例子为
 * （1）这个例子是在activity点击一首歌时  PlayingBarEvent playingBarEvent = new PlayingBarEvent();
 * playingBarEvent.setPosition(position);//传进歌曲在列表中的位置
 * playingBarEvent.setSongsBeanList(songsRV_dataList);传进歌曲列表
 * playingBarEvent.setPlayingStatus(PlayingBarEvent.PLAYANEW);//传进界面模式
 * HandleBottomBar.changBottomBarView(playingBarEvent);
 * 还差SeekBar没有设置
 * Created by CNT on 2018/2/4.
 */

public class HandleBottomBar {

    private static Context context;

    private static TextView bottomBar_songName;
    private static TextView bottomBar_singer;
    private static Button bottomBar_playButton;
    private static ImageView bottomBar_image;
    private static Button bottomBar_playingLayout_button;//播放界面中的播放按钮
    private static TextView bottomBar_palying_songs_name;//播放界面中的歌曲名
    private static TextView bottomBar_playing_song_length;
    private static Button bottomBar_playing_nextSong;
    private static Button bottomBar_playing_lastSong;
    private static SeekBar seekBar;

    public static final int PLAYTOPAUSE = 1;
    public static final int PAUSETOPLAY = 2;
    public static final int PLAYANEW = 3;
    public static final int PLAYNEXT = 4;
    public static final int PLAYLAST = 5;

    public static boolean isPlaying = false;
    public static PlayingBarEvent staticPlayingBarEvent;

    public HandleBottomBar(AppCompatActivity context) {
        initView(context);
        this.context = context;
    }

    public static void handleClick() {
        bottomBar_playingLayout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    MusicEvent musicEvent = new MusicEvent();
                    musicEvent.setPlayintStatus(MusicEvent.PAUSE);
                    EventBus.getDefault().postSticky(musicEvent);
                    PlayingBarEvent playingBarEvent = new PlayingBarEvent();
                    playingBarEvent.setPlayingStatus(PlayingBarEvent.PLAYTOPAUSE);
                    //EventBus.getDefault().postSticky(playingBarEvent);
                    changBottomBarView(playingBarEvent);
                    isPlaying = false;
                } else {
                    MusicEvent musicEvent = new MusicEvent();
                    musicEvent.setPlayintStatus(MusicEvent.PAUSETOPLAY);
                    EventBus.getDefault().postSticky(musicEvent);
                    PlayingBarEvent playingBarEvent = new PlayingBarEvent();
                    playingBarEvent.setPlayingStatus(PlayingBarEvent.PAUSETOPLAY);
                    //EventBus.getDefault().postSticky(playingBarEvent);
                    changBottomBarView(playingBarEvent);
                    isPlaying = true;
                }
            }
        });
        bottomBar_playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    MusicEvent musicEvent = new MusicEvent();
                    musicEvent.setPlayintStatus(MusicEvent.PAUSE);
                    EventBus.getDefault().postSticky(musicEvent);
                    PlayingBarEvent playingBarEvent = new PlayingBarEvent();
                    playingBarEvent.setPlayingStatus(PlayingBarEvent.PLAYTOPAUSE);
                    //EventBus.getDefault().postSticky(playingBarEvent);
                    changBottomBarView(playingBarEvent);
                    isPlaying = false;
                } else {
                    MusicEvent musicEvent = new MusicEvent();
                    musicEvent.setPlayintStatus(MusicEvent.PAUSETOPLAY);
                    EventBus.getDefault().postSticky(musicEvent);
                    PlayingBarEvent playingBarEvent = new PlayingBarEvent();
                    playingBarEvent.setPlayingStatus(PlayingBarEvent.PAUSETOPLAY);
                    //EventBus.getDefault().postSticky(playingBarEvent);
                    changBottomBarView(playingBarEvent);
                    isPlaying = true;
                }
            }
        });
        bottomBar_playing_nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicEvent musicEvent = new MusicEvent();
                musicEvent.setPlayintStatus(MusicEvent.PLAYNEXT);
                EventBus.getDefault().postSticky(musicEvent);

                PlayingBarEvent playingBarEvent = new PlayingBarEvent();
                playingBarEvent.setPlayingStatus(PlayingBarEvent.PLAYNEXT);
                //EventBus.getDefault().postSticky(playingBarEvent);
                changBottomBarView(playingBarEvent);
                isPlaying = true;
            }
        });
        bottomBar_playing_lastSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicEvent musicEvent = new MusicEvent();
                musicEvent.setPlayintStatus(MusicEvent.PLAYLAST);
                EventBus.getDefault().postSticky(musicEvent);

                PlayingBarEvent playingBarEvent = new PlayingBarEvent();
                playingBarEvent.setPlayingStatus(PlayingBarEvent.PLAYLAST);
                //EventBus.getDefault().postSticky(playingBarEvent);
                changBottomBarView(playingBarEvent);
                isPlaying = true;
            }
        });

    }

    private static void initView(AppCompatActivity context) {
        bottomBar_songName = (TextView) context.findViewById(R.id.bottomBar_songName);
        bottomBar_singer = (TextView) context.findViewById(R.id.bottomBar_singer);
        bottomBar_playButton = (Button) context.findViewById(R.id.bottomBar_play_button);
        bottomBar_image = (ImageView) context.findViewById(R.id.play_image);
        bottomBar_playingLayout_button = (Button) context.findViewById(R.id.play_button);
        bottomBar_palying_songs_name = (TextView) context.findViewById(R.id.palying_songs_name);
        bottomBar_playing_song_length = (TextView) context.findViewById(R.id.palying_song_length);
        bottomBar_playing_nextSong = (Button) context.findViewById(R.id.nextsong_button);
        bottomBar_playing_lastSong = (Button) context.findViewById(R.id.lastsong_button);
        seekBar = (SeekBar) context.findViewById(R.id.seekbar);
    }

    public static void changBottomBarView(PlayingBarEvent playingBarEvent) {
        switch (playingBarEvent.getPlayingStatus()) {
            case HandleBottomBar.PLAYTOPAUSE: {
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_play_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_play_button);
                //seekBar要设置停住
            }
            break;
            case HandleBottomBar.PAUSETOPLAY: {
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                //seekBar设置恢复移动
            }
            break;
            case HandleBottomBar.PLAYANEW: {
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_palying_songs_name.setText(playingBarEvent.getSongsBeanList().get(playingBarEvent.getPosition()).getName());
                bottomBar_songName.setText(playingBarEvent.getSongsBeanList().get(playingBarEvent.getPosition()).getName());
                bottomBar_singer.setText(playingBarEvent.getSongsBeanList().get(playingBarEvent.getPosition()).getAuthor());
                bottomBar_playing_song_length.setText(playingBarEvent.getSongsBeanList().get(playingBarEvent.getPosition()).getFormatLenght());
                GetCoverUtil.setCover(context, playingBarEvent.getSongsBeanList().get(playingBarEvent.getPosition()), bottomBar_image, 200);
            }
            break;
            case HandleBottomBar.PLAYNEXT: {
                int position = (staticPlayingBarEvent.getPosition() + 1) % staticPlayingBarEvent.getSongsBeanList().size();
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_palying_songs_name.setText(staticPlayingBarEvent.getSongsBeanList().get(position).getName());
                bottomBar_songName.setText(staticPlayingBarEvent.getSongsBeanList().get(position).getName());
                bottomBar_singer.setText(staticPlayingBarEvent.getSongsBeanList().get(position).getAuthor());
                bottomBar_playing_song_length.setText(staticPlayingBarEvent.getSongsBeanList().get(position).getFormatLenght());
                GetCoverUtil.setCover(context, staticPlayingBarEvent.getSongsBeanList().get(position), bottomBar_image, 200);
                staticPlayingBarEvent.setPosition(position);
            }
            break;
            case HandleBottomBar.PLAYLAST: {
                int position = (staticPlayingBarEvent.getPosition() - 1 + staticPlayingBarEvent.getSongsBeanList().size()) % staticPlayingBarEvent.getSongsBeanList().size();
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_palying_songs_name.setText(staticPlayingBarEvent.getSongsBeanList().get(position).getName());
                bottomBar_songName.setText(staticPlayingBarEvent.getSongsBeanList().get(position).getName());
                bottomBar_singer.setText(staticPlayingBarEvent.getSongsBeanList().get(position).getAuthor());
                bottomBar_playing_song_length.setText(staticPlayingBarEvent.getSongsBeanList().get(position).getFormatLenght());
                GetCoverUtil.setCover(context, staticPlayingBarEvent.getSongsBeanList().get(position), bottomBar_image, 200);
                staticPlayingBarEvent.setPosition(position);
            }
            default:
                break;
        }
    }


}
