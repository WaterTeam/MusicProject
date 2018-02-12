package com.waterteam.musicproject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterteam.musicproject.bean.AllMediaBean;

import java.util.ArrayList;
import java.util.List;

import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.customview.BottomBar;
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
    private BottomBar bottomBar;

    private TextView bottomBar_songName;
    private TextView bottomBar_singer;
    private Button bottomBar_playButton;
    private ImageView bottomBar_image;
    private Button bottomBar_playingLayout_button;//播放界面中的播放按钮
    private TextView bottomBar_palying_songs_name;//播放界面中的歌曲名
    private TextView bottomBar_playing_song_length;
    private TextView bottomBar_now_play_time;
    private Button play_mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置为沉浸式状态栏，设置了状态栏颜色及字体颜色
        StatusBarUtil.setStatusBarLightMode(this);

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
      //  initBottomBar();
        initFirstSong();
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
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        handleBottomBar();
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

    private void initFirstSong() {

        AllMediaBean mySongs = AllMediaBean.getInstance();
        if (mySongs.getSongs().size()>0) {
            EventFromTouch eventFromTouch = new EventFromTouch();
            eventFromTouch.setSong(mySongs.getSongs().get(0));
            eventFromTouch.setSongs(mySongs.getSongs());
            eventFromTouch.setPosition(0);
            eventFromTouch.setStatu(EventFromTouch.NOW_PLAY);
            EventBus.getDefault().post(eventFromTouch);

            EventFromBar eventFromBar = new EventFromBar();
            eventFromBar.setStatu(EventFromBar.PAUSE);
            EventBus.getDefault().post(eventFromBar);
        }
    }

    private void initBottomBar() {
        bottomBar_songName = (TextView) findViewById(R.id.bottomBar_songName);
        bottomBar_singer = (TextView) findViewById(R.id.bottomBar_singer);
        bottomBar_playButton = (Button) findViewById(R.id.bottomBar_play_button);
        bottomBar_image = (ImageView) findViewById(R.id.play_image);
        bottomBar_playingLayout_button = (Button) findViewById(R.id.play_button);
        bottomBar_palying_songs_name = (TextView) findViewById(R.id.palying_songs_name);
        bottomBar_playing_song_length = (TextView) findViewById(R.id.palying_song_length);
        play_mode = (Button) findViewById(R.id.play_mode);
    }

    private void handleBottomBar() {
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
            GetCoverUtil.setCover(this, songsBean, bottomBar_image, 600);
            switch (PlayService.playMode) {
                case EventFromBar.LISTMODE:
                    play_mode.setBackgroundResource(R.drawable.ic_liebiao);
                    break;
                case EventFromBar.SIMPLEMODE:
                    play_mode.setBackgroundResource(R.drawable.ic_danqu);
                    break;
                case EventFromBar.RANDOMMODE:
                    play_mode.setBackgroundResource(R.drawable.ic_suiji);
                    break;
                default:
                    break;
            }
        }
    }
}

