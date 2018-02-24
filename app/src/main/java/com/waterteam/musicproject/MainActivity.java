package com.waterteam.musicproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bastatusbar.BAStatusBar;
import com.waterteam.musicproject.bean.AllMediaBean;

import java.util.ArrayList;
import java.util.List;

import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.customview.BottomBar;
import com.waterteam.musicproject.customview.MyNotification;
import com.waterteam.musicproject.eventsforeventbus.EventFromBar;
import com.waterteam.musicproject.eventsforeventbus.EventFromTouch;
import com.waterteam.musicproject.eventsforeventbus.EventToBarFromService;
import com.waterteam.musicproject.service.playmusic.service.PlayService;
import com.waterteam.musicproject.util.GetCoverUtil;
import com.waterteam.musicproject.util.StatusBarUtil;
import com.waterteam.musicproject.viewpagers.MyPageAdapter;
import com.waterteam.musicproject.viewpagers.artist.page.ArtistPageFragment;
import com.waterteam.musicproject.viewpagers.songs.page.SongsPageFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean debug = false;
    ViewPager viewPager;
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    MyPageAdapter fragmentPagerAdapter;
    MyNotification myNotification;
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置为沉浸式状态栏，设置了状态栏颜色及字体颜色
        StatusBarUtil.setStatusBarLightMode(this);
        new BAStatusBar().setfitsSystemWindowsBar(this);

        AllMediaBean mySongsData;
        //为了解决程序被杀死，再回来后空指针异常的问题我希望你这样再处理下数据源，反正这里必须要这样写
        if (savedInstanceState != null) {
            mySongsData = (AllMediaBean) savedInstanceState.getSerializable("datas");
            AllMediaBean.getInstance().setArtists(mySongsData.getArtists());
            AllMediaBean.getInstance().setSongs(mySongsData.getSongs());
            Log.d(TAG, "从保存的获取");
        } else if (debug) { //下面的代码你写不写都行，我只是测试而已
            mySongsData = AllMediaBean.getInstance();
            Log.d(TAG, "艺术家=" + mySongsData.getArtists().size());
            Log.d(TAG, "歌曲=" + mySongsData.getSongs().size());
        }
        initView();
        //initNotification();
        myNotification = new MyNotification(this);
        myNotification.initAndNotify();
    }


    //必须写该方法
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("datas", AllMediaBean.getInstance());
    }

    @Override
    public void onBackPressed() {
        if (bottomBar.getIsPullUp()) {
            bottomBar.pullDown();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        myNotification.cancel();
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        // handleBottomBar();
        super.onResume();
    }

    /**
     * initView()方法用于初始化主活动界面
     *
     * @param
     * @return
     * @throws
     * @author CNT on 2017/12/5.
     */
    private void initView() {
        viewPager = (ViewPager) this.findViewById(R.id.viewPager_MainActivity);
        bottomBar = (BottomBar) this.findViewById(R.id.MainActivity_bottomBar);

        //往viewPager的数据列表中添加2个碎片；
        fragmentList.add(new ArtistPageFragment());
        fragmentList.add(new SongsPageFragment());

        fragmentPagerAdapter = new MyPageAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    private void initNotification() {
        //        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
        RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.big_notification);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        remoteViews.setImageViewResource(R.id.notification_image, (int) PlayService.NowPlaySong.getAlbumId());
        remoteViews.setTextViewText(R.id.notification_song, PlayService.NowPlaySong.getName());
        remoteViews.setTextViewText(R.id.notification_singer, PlayService.NowPlaySong.getAuthor());
        remoteView.setImageViewResource(R.id.big_notification_image, (int) PlayService.NowPlaySong.getAlbumId());
        remoteView.setTextViewText(R.id.big_notification_song, PlayService.NowPlaySong.getName());
        remoteView.setTextViewText(R.id.big_notification_singer, PlayService.NowPlaySong.getAuthor());
//        Notification notification = new NotificationCompat.Builder(this)
//                .setContentIntent(pendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setCustomBigContentView(contentViews)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .build();//先创建通知对象
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        manager.notify(1, notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Intent intent = new Intent(this, MainActivity.class);
        // 点击跳转到主界面
//        PendingIntent intent_go = PendingIntent.getActivity(this, 5, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.notice, intent_go);

        // 4个参数context, requestCode, intent, flags
//        PendingIntent intent_close = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.widget_close, intent_close);

        // 设置上一曲
        Intent prv = new Intent();
        prv.setAction("LastSong");
        PendingIntent intent_prev = PendingIntent.getBroadcast(this, 1, prv,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_lastButton, intent_prev);

        // 设置播放
        if (PlayService.isPlay) {
            Intent playorpause = new Intent();
            playorpause.setAction("SongPause");
            PendingIntent intent_play = PendingIntent.getBroadcast(this, 2,
                    playorpause, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.notification_playButton, intent_play);
        }
        if (!PlayService.isPlay) {
            Intent playorpause = new Intent();
            playorpause.setAction("SongPlay");
            PendingIntent intent_play = PendingIntent.getBroadcast(this, 6, playorpause, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.notification_playButton, intent_play);
        }

        // 下一曲
        Intent next = new Intent();
        next.setAction("NextSong");
        PendingIntent intent_next = PendingIntent.getBroadcast(this, 3, next, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_nextButton, intent_next);

        // 设置收藏
//        PendingIntent intent_fav = PendingIntent.getBroadcast(this, 4, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.widget_fav, intent_fav);

        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setCustomContentView(remoteViews);
        builder.setCustomBigContentView(remoteView);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        Notification notify = builder.build();

//        notify.contentView = remoteViews; // 设置下拉图标
//        notify.bigContentView = remoteViews; // 防止显示不完全,需要添加apisupport
        notify.flags = Notification.FLAG_ONGOING_EVENT;
//        notify.icon = R.drawable.notification_bar_icon;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, notify);
    }
}

