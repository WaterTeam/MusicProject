package com.waterteam.musicproject.service.playmusic.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.waterteam.musicproject.MainActivity;
import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.eventsforeventbus.MusicEvent;
import com.waterteam.musicproject.util.HandleBottomBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class PlayMusicService extends Service {
    private static AppCompatActivity mainActivity = null;

    private MediaPlayer mediaPlayer;
    private List<SongsBean> songsBeanList;
    private int position;

    public static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //处理消息
            Bundle bundle=msg.getData();
            //获取歌曲长度和当前播放位置，并设置到进度条上
            int duration=bundle.getInt("duration");
            int currentposition=bundle.getInt("currentposition");
            //seekBar.setMax(duration);
            //seekBar.setProgress(currentposition);
        }
    };


    public PlayMusicService() {
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("PlayMusicService", "测试服务1");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("WaterMusic")
                .setContentText("歌曲")
                //.setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build();//先创建通知对象
        startForeground(1, notification);
        EventBus.getDefault().register(this);
        //new HandleBottomBar(mainActivity).handleClick();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("PlayMusicService", "测试服务2");
        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void playMusic(MusicEvent messageEvent) {
        Log.e("PlayMusicService", "测试EventBus");
        switch (messageEvent.getPlayintStatus()) {
            case MusicEvent.PLAY: {//播放音乐前，要先停止；播放需要知道播放列表，及播放歌手在列表中的position
                songsBeanList = messageEvent.getSongsBeanList();
                position = messageEvent.getPosition();
                if (!"".equals(songsBeanList.get(position).getLocation())) {
                    try {
                        mediaPlayer.setDataSource(songsBeanList.get(position).getLocation());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            case MusicEvent.PAUSE:
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            case MusicEvent.STOP:
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                }
                break;
            case MusicEvent.PAUSETOPLAY: {
                mediaPlayer.start();
            }
            break;
            case MusicEvent.PLAYNEXT: {//要先设置停止
                mediaPlayer.reset();
                position = (position + 1) % songsBeanList.size();
                if (!"".equals(songsBeanList.get(position).getLocation())) {
                    try {
                        mediaPlayer.setDataSource(songsBeanList.get(position).getLocation());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            case MusicEvent.PLAYLAST: {
                mediaPlayer.reset();
                position = (position - 1 + songsBeanList.size()) % songsBeanList.size();
                if (!"".equals(songsBeanList.get(position).getLocation())) {
                    try {
                        mediaPlayer.setDataSource(songsBeanList.get(position).getLocation());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            EventBus.getDefault().unregister(this);
        }
    }

    static public void setMainActivity(AppCompatActivity mainActivity1) {
        mainActivity = mainActivity1;
    }
}
