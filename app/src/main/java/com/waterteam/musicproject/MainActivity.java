package com.waterteam.musicproject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.waterteam.musicproject.bean.AllMediaBean;

import java.util.ArrayList;
import java.util.List;

import com.waterteam.musicproject.customview.BottomBar;
import com.waterteam.musicproject.eventsforeventbus.PlayingBarEvent;
import com.waterteam.musicproject.service.playmusic.service.PlayMusicService;
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
        PlayMusicService.setMainActivity(this);
        startService(new Intent(this, PlayMusicService.class));
        EventBus.getDefault().register(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void ononMoonStickyEvent(PlayingBarEvent playingBarEvent){
        bottomBar.playANewSong(playingBarEvent);
    }

}

