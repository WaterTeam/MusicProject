package com.waterteam.musicproject.service.playmusic.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.waterteam.musicproject.MainActivity;
import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.bean.WaitingPlaySongs;
import com.waterteam.musicproject.eventsforeventbus.EventFromBar;
import com.waterteam.musicproject.eventsforeventbus.EventFromTouch;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PlayService extends Service {
    private MediaPlayer mediaPlayer; //播放音乐用的
    private int position=0; //当前播放音乐在播放列表中的位置
    WaitingPlaySongs playList; //播放列表
    private static final String TAG = "PlayService";

    public PlayService() {
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
        getPlayList();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 获取播放列表
     * @author BA on 2018/2/6 0006
     * @param
     * @return
     * @exception
     */
    public void getPlayList() {
        playList = AllMediaBean.getInstance().getWaitingPlaySongs();
    }

    /**
     *  来自控制播放的Bar的事件处理,注意，来自播放bar的事件只需要有状态即可
     *  ，当然如果是seekbar要有进度，进度条的statu我在事件里面还没写
     *  事件类是 {@link EventFromBar}，你自己去加
     * @author BA on 2018/2/6 0006
     * @param event {@link EventFromBar}
     * @return
     * @exception
     */
    @Subscribe
    public void eventFromBar(EventFromBar event) {
        Log.d(TAG, "eventFromBar: ");
        switch (event.getStatu()) {
            case EventFromBar.PLAY:
                Log.d(TAG, "eventFromBar: play");
                playSong();
                break;
            case EventFromBar.PAUSE:
                Log.d(TAG, "eventFromBar: pause");
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            case EventFromBar.PLAYNEXT:
                Log.d(TAG, "eventFromBar: next");
                mediaPlayer.reset();
                position += position;
                playSong();
                break;
            case EventFromBar.PLAYLAST:
                Log.d(TAG, "eventFromBar: last");
                position-=position;
                playSong();
                break;
            case EventFromBar.PAUSETOPLAY:
                Log.d(TAG, "eventFromBar: paust 2 play");
                mediaPlayer.start();
                playSong();
                break;
            case EventFromBar.STOP:
                Log.d(TAG, "eventFromBar: stop");
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                }
                break;
        }
    }

    /**
     * 处理点击歌曲，长按歌曲事件的方法,注意，这里我只是处理了添加到播放列表的方法
     * 播放的逻辑你来写
     * @author BA on 2018/2/7 0007
     * @param event {@link EventFromTouch}
     * @return
     * @exception
     */
    @Subscribe
    public void EventFromTouch(EventFromTouch event){
        switch (event.getStatu()){
            case EventFromTouch.NOW_PLAY:
                //比如说列表中有abcd，当前播放的是b，那么既然是该事件就是要求马上播放
                //传入的歌曲是f，那么f插入到播放列表后播放列表会变成abfcd，所以下面是++position
                //你只要继续使用这个position播放就好
                boolean success = playList.addSongToPosition(++position,event.getSong());
                if (!success){
                    Toast.makeText(this, "列表中已经有该歌曲", Toast.LENGTH_SHORT).show();
                }
                break;
            case EventFromTouch.ADD_ALL_TO_LIST:
                playList.addList(event.getSongs());
                break;
            case EventFromTouch.ADD_TO_LIST:
                boolean success2foot= playList.addSongsToFoot(event.getSong());
                if (!success2foot){
                    Toast.makeText(this, "列表中已经有该歌曲", Toast.LENGTH_SHORT).show();
                }else {
                    position=0;
                }
                break;
            case EventFromTouch.ADD_TO_NEXT:
                boolean success2next= playList.addSongToPosition(position+1,event.getSong());
                if (!success2next){
                    Toast.makeText(this, "列表中已经有该歌曲", Toast.LENGTH_SHORT).show();
                }
                break;
            case EventFromTouch.DELETE_FROM_LIST:
                playList.removeSong(event.getSong());
                break;
        }
    }

    /**
     * 播放音乐，这是你上次写的，我把他独立出来，然后有没有问题我不知道，没有测试
     * @author BA on 2018/2/6 0006
     * @param
     * @return
     * @exception
     */
    private void playSong() {
        if (position>=playList.getSongsCount()){
            Toast.makeText(this, "播放列表中没有歌曲！", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "playSong: noSong");
        }else{
            SongsBean songsBean = playList.getSong(position);
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
}
