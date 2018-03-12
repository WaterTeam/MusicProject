package com.waterteam.musicproject.service.playmusic.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.waterteam.musicproject.MainActivity;
import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.bean.WaitingPlaySongs;
import com.waterteam.musicproject.bean.WaitingPlaySongsLayouts;
import com.waterteam.musicproject.eventsforeventbus.EventFromBar;
import com.waterteam.musicproject.eventsforeventbus.EventFromTouch;
import com.waterteam.musicproject.eventsforeventbus.EventToBarFromService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayService extends Service {
    private MediaPlayer mediaPlayer; //播放音乐用的
    public static int position = 0; //当前播放音乐在播放列表中的位置
    private int nextPosition = 0;//用户指定的下一首的位置，记住这个位置是为了用户指定了一个下一首之后，再指定了下一个下一首
    private int nextSongCount = 0;//记录用户连续点击了指定某首歌为下一首的次数

    public static int playMode = EventFromBar.LISTMODE;//默认列表循环
    public static WaitingPlaySongs playList = AllMediaBean.getInstance().getWaitingPlaySongs(); //播放列表
    public static WaitingPlaySongsLayouts waitingPlaySongsLayouts;

    int randomListPosition = -1;//记录随机播放的上一首的位置
    private boolean iff = false;//用于randomLastPlay()方法
    private static final String TAG = "PlayService";
    private static boolean ISFIRST = true;
    public static SongsBean NowPlaySong;
    public static boolean isPlay = false;


    public PlayService() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//播放完后默认播放下一首
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                playNext();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        waitingPlaySongsLayouts = new WaitingPlaySongsLayouts(getApplicationContext());
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("WaterMusic")
                .setContentText("歌曲")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build();//先创建通知对象
        startForeground(1, notification);

        EventBus.getDefault().register(this);
        waitingPlaySongsLayouts = new WaitingPlaySongsLayouts(getApplicationContext());
        waitingPlaySongsLayouts.addList(playList.getSongs());
        // getPlayList();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 获取播放列表
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/6 0006
     */
    public void getPlayList() {
        playList = AllMediaBean.getInstance().getWaitingPlaySongs();
    }

    /**
     * 来自控制播放的Bar的事件处理,注意，来自播放bar的事件只需要有状态即可
     * ，当然如果是seekbar要有进度，进度条的statu我在事件里面还没写
     * 事件类是 {@link EventFromBar}，你自己去加
     *
     * @param event {@link EventFromBar}
     * @return
     * @throws
     * @author BA on 2018/2/6 0006
     */
    @Subscribe
    public void eventFromBar(EventFromBar event) {
        EventToBarFromService eventto = new EventToBarFromService();
        Log.d(TAG, "eventFromBar: ");
        switch (event.getStatu()) {
            case EventFromBar.PLAY://??????
                mediaPlayer.reset();
                Log.d(TAG, "eventFromBar: play");
                playSong();
                break;
            case EventFromBar.PAUSE:
                Log.d(TAG, "eventFromBar: pause");
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    isPlay = false;
                }
                eventto.setStatu(EventToBarFromService.PAUSE);
                EventBus.getDefault().post(eventto);
                break;
            case EventFromBar.PLAYNEXT:
                playNext();
                if (!ISFIRST) {
                    mediaPlayer.start();
                } else {
                    playSong();
                    StartProgress();
                    ISFIRST = false;
                }
                break;
            case EventFromBar.PLAYLAST:
                playLast();
                if (!ISFIRST) {
                    mediaPlayer.start();
                } else {
                    playSong();
                    StartProgress();
                    ISFIRST = false;
                }
                break;
            case EventFromBar.PAUSETOPLAY:
                Log.d(TAG, "eventFromBar: paust 2 play");
                isPlay = true;
                if (!ISFIRST) {
                    mediaPlayer.start();
                } else {
                    playSong();
                    StartProgress();
                    ISFIRST = false;
                }

                eventto.setStatu(EventToBarFromService.PAUSETOPLAY);
                EventBus.getDefault().post(eventto);
                break;
            case EventFromBar.STOP:
                Log.d(TAG, "eventFromBar: stop");
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                }
                break;
            case EventFromBar.SEEKBARMOVE:
                if (!ISFIRST) {
                } else {
                    playSong();
                    StartProgress();
                    mediaPlayer.pause();
                    ISFIRST = false;
                }
                mediaPlayer.seekTo(event.getProgress());
                break;
            case EventFromBar.LISTMODE:
                playMode = event.getStatu();
                break;
            case EventFromBar.SIMPLEMODE:
                playMode = event.getStatu();
                break;
            case EventFromBar.RANDOMMODE:
                playMode = event.getStatu();
                break;
            case EventFromBar.VIEWPAGERMOVE:
                mediaPlayer.reset();
                isPlay = true;
                nextPosition = position;
                playSong();
                if (!ISFIRST) {
                } else {
                    StartProgress();
                    ISFIRST = false;
                }
                eventto.setStatu(EventToBarFromService.MOVEVIewPAGER);
                eventto.setSongsBeanList(playList.getSongs());
                eventto.setPosition(position);
                EventBus.getDefault().post(eventto);
                break;
            default:
                break;
        }
    }

    /**
     * 处理点击歌曲，长按歌曲事件的方法,注意，这里我只是处理了添加到播放列表的方法
     * 播放的逻辑你来写
     *
     * @param event {@link EventFromTouch}
     * @return
     * @throws
     * @author BA on 2018/2/7 0007
     */
    @Subscribe
    public void EventFromTouch(final EventFromTouch event) {
        final EventToBarFromService eventto = new EventToBarFromService();
        switch (event.getStatu()) {
            case EventFromTouch.NOW_PLAY:
                if (event.getSongs() != null) {
                    playList.addList(event.getSongs());
                }
                position = event.getPosition();
                nextPosition = position;
                nextSongCount = 0;
                mediaPlayer.reset();
                playSong();

                eventto.setStatu(EventToBarFromService.PLAYANEW);
                eventto.setSongsBeanList(playList.getSongs());
                eventto.setPosition(position);
                EventBus.getDefault().post(eventto);
                if (ISFIRST) {
                    StartProgress();
                    ISFIRST = false;
                }
                break;
            case EventFromTouch.ADD_ALL_TO_LIST:
                playList.addList(event.getSongs());
                waitingPlaySongsLayouts.addList(event.getSongs());
                position = 0;
                mediaPlayer.reset();
                playSong();
                break;
            case EventFromTouch.ADD_TO_LIST:
                playList.addSongsToFoot(event.getSong());
                waitingPlaySongsLayouts.addLayoutToFoot(event.getSong());
                break;
            case EventFromTouch.ADD_TO_NEXT:
                playList.addSongToPosition(++nextPosition, event.getSong());
                waitingPlaySongsLayouts.addSongToPosition(nextPosition, event.getSong());
                nextSongCount++;
                break;
            case EventFromTouch.DELETE_FROM_LIST:
                playList.removeSong(event.getSong());
                waitingPlaySongsLayouts.removeSong(playList.getSongs().indexOf(event.getSong()));
                break;
            case EventFromTouch.ALWAYS_PLAY:
                playList.addSongToPosition(++position, event.getSong());
                waitingPlaySongsLayouts.addSongToPosition(position, event.getSong());
                nextPosition = position;
                mediaPlayer.reset();
                playSong();

                playMode = EventFromBar.SIMPLEMODE;
                eventto.setStatu(EventToBarFromService.PLAYANEW);
                eventto.setSongsBeanList(playList.getSongs());
                eventto.setPosition(position);
                eventto.setPlayMode(playMode);
                EventBus.getDefault().post(eventto);

                if (ISFIRST) {
                    StartProgress();
                    ISFIRST = false;
                }
                break;
        }
    }

    /**
     * 播放音乐，这是你上次写的，我把他独立出来，然后有没有问题我不知道，没有测试
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/6 0006
     */
    private void playSong() {
        if (position >= playList.getSongsCount() || position < 0) {
            Toast.makeText(this, "播放列表中没有歌曲！", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "playSong: noSong");
        } else {
            SongsBean songsBean = playList.getSong(position);
            NowPlaySong = songsBean;
            isPlay = true;
            try {
                mediaPlayer.setDataSource(songsBean.getLocation());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public void StartProgress() {
        //开辟新的Thread用于定期刷新SeekBar;
        DelayThread dThread = new DelayThread(600);
        dThread.start();
    }

    //开启一个线程进行seekbar实时刷新
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            EventToBarFromService eventto = new EventToBarFromService();
            eventto.setStatu(EventToBarFromService.SEEKBARMOVEITSELF);
            eventto.setPosition(position);
            eventto.setSongsBeanList(playList.getSongs());
            eventto.setProgress(mediaPlayer.getCurrentPosition());
            EventBus.getDefault().post(eventto);
        }
    };

    public class DelayThread extends Thread {
        int milliseconds;

        public DelayThread(int i) {
            milliseconds = i;
        }

        public void run() {
            while (true) {
                try {
                    sleep(milliseconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }
    }

    private void listNextPlay() {
        mediaPlayer.reset();
        position = (position + 1) % playList.getSongs().size();//默认列表循环
        playSong();

        EventToBarFromService eventto = new EventToBarFromService();
        eventto.setStatu(EventToBarFromService.PLAYANEW);
        eventto.setSongsBeanList(playList.getSongs());
        eventto.setPosition(position);
        EventBus.getDefault().post(eventto);
    }

    private void listLastPlay() {
        mediaPlayer.reset();
        position = (position - 1 + playList.getSongs().size()) % playList.getSongs().size();
        playSong();

        EventToBarFromService eventto = new EventToBarFromService();
        eventto.setStatu(EventToBarFromService.PLAYANEW);
        eventto.setSongsBeanList(playList.getSongs());
        eventto.setPosition(position);
        EventBus.getDefault().post(eventto);
    }

    private void simplePlay() {
        mediaPlayer.reset();
        playSong();

        EventToBarFromService eventto = new EventToBarFromService();
        eventto.setStatu(EventToBarFromService.PLAYANEW);
        eventto.setSongsBeanList(playList.getSongs());
        eventto.setPosition(position);
        EventBus.getDefault().post(eventto);
    }

    private void randomNextPlay() {

        randomListPosition = position;
        mediaPlayer.reset();
        Random random = new Random();
        position = random.nextInt(playList.getSongs().size());
        playSong();


        EventToBarFromService eventto = new EventToBarFromService();
        eventto.setStatu(EventToBarFromService.PLAYANEW);
        eventto.setSongsBeanList(playList.getSongs());
        eventto.setPosition(position);
        EventBus.getDefault().post(eventto);
        iff = true;
    }

    private void randomLastPlay() {
        if (!iff) {
            randomNextPlay();
        } else {
            mediaPlayer.reset();

            SongsBean songsBean = playList.getSong(randomListPosition);
            try {
                mediaPlayer.setDataSource(songsBean.getLocation());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            EventToBarFromService eventto = new EventToBarFromService();
            eventto.setStatu(EventToBarFromService.PLAYANEW);
            eventto.setSongsBeanList(playList.getSongs());
            eventto.setPosition(randomListPosition);
            EventBus.getDefault().post(eventto);

            iff = false;
        }
    }

    /**
     * 处理3种播放模式下的下一首播放和上一首播放
     *
     * @param
     * @return
     * @throws
     * @author CNT on 2018/2/10.
     */

    private void playNext() {
        isPlay = true;
        switch (playMode) {
            case EventFromBar.LISTMODE:
                listNextPlay();
                nextPosition = position;
                break;
            case EventFromBar.SIMPLEMODE:
                simplePlay();
                nextPosition = position;
                break;
            case EventFromBar.RANDOMMODE:
                if (nextSongCount > 0) {//当用户有了下一首的动作之后，则此时下一首应该为用户选定的歌曲（即使是随机播放模式）
                    listNextPlay();
                    nextSongCount--;
                } else {
                    randomNextPlay();
                    nextPosition = position;
                }
                break;
            default:
                break;
        }
        nextPosition = position;
    }

    private void playLast() {
        switch (playMode) {
            case EventFromBar.LISTMODE:
                listLastPlay();
                break;
            case EventFromBar.SIMPLEMODE:
                simplePlay();
                break;
            case EventFromBar.RANDOMMODE:
                randomLastPlay();
                break;
            default:
                break;
        }
        nextPosition = position;
    }
}