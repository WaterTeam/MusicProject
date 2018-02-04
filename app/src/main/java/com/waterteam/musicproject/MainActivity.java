package com.waterteam.musicproject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.waterteam.musicproject.bean.AllMediaBean;

import java.util.ArrayList;
import java.util.List;

import com.waterteam.musicproject.customview.BottomBar;
import com.waterteam.musicproject.eventsforeventbus.PlayingBarEvent;
import com.waterteam.musicproject.service.playmusic.service.PlayMusicService;
import com.waterteam.musicproject.util.HandleBottomBar;
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
    BottomBar bottomBar;

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

        //开启播放音乐服务
        startService(new Intent(this, PlayMusicService.class));
        //控制bottomBar
        EventBus.getDefault().register(this);
        //设置bottomBar按钮点击事件
        new HandleBottomBar(this).handleClick();
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
        stopService(new Intent(this, PlayMusicService.class));
        EventBus.getDefault().unregister(this);
        super.onDestroy();

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


    /**
     *作用于EventBus,用于设置bootomBar的控件内容
     *
     * @author CNT on 2018/2/5.
     * @param
     * @return
     * @exception
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void setBottomBar(PlayingBarEvent playingBarEvent) {
        //根据点击区分；
        switch (playingBarEvent.getPlayingStatus()) {
            case PlayingBarEvent.PLAYTOPAUSE: {
                HandleBottomBar.changBottomBarView(PlayingBarEvent.PLAYTOPAUSE, playingBarEvent);
            }
            break;
            case PlayingBarEvent.PAUSETOPLAY: {
                HandleBottomBar.changBottomBarView(PlayingBarEvent.PAUSETOPLAY, playingBarEvent);
            }
            break;
            case PlayingBarEvent.PLAYANEW: {
                HandleBottomBar.changBottomBarView(PlayingBarEvent.PLAYANEW, playingBarEvent);
            }
            break;
            case PlayingBarEvent.PLAYNEXT: {
                HandleBottomBar.changBottomBarView(PlayingBarEvent.PLAYNEXT, playingBarEvent);
            }
            break;
            case PlayingBarEvent.PLAYLAST: {
                HandleBottomBar.changBottomBarView(PlayingBarEvent.PLAYLAST, playingBarEvent);
            }
            default:
                break;
        }
    }
}

