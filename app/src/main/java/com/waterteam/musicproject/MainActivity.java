package com.waterteam.musicproject;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.bastatusbar.BAStatusBar;
import com.waterteam.musicproject.bean.AllMediaBean;

import java.util.ArrayList;
import java.util.List;

import com.waterteam.musicproject.customview.bottom.bar.BottomBar;
import com.waterteam.musicproject.customview.MyNotification;
import com.waterteam.musicproject.customview.bottom.bar.playing.PlayTouchUtil;

import com.waterteam.musicproject.util.StatusBarUtil;
import com.waterteam.musicproject.viewpagers.MyPageAdapter;
import com.waterteam.musicproject.viewpagers.artist.page.ArtistPageFragment;
import com.waterteam.musicproject.viewpagers.songs.page.SongsPageFragment;

import org.greenrobot.eventbus.EventBus;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean debug = false;
    ViewPager viewPager;
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    MyPageAdapter fragmentPagerAdapter;
    MyNotification myNotification;
    private BottomBar bottomBarPlaying;
    private BottomBar second_bottomBar;

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
        if (bottomBarPlaying.getIsPullUp()) {
            bottomBarPlaying.pullDown();
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
        bottomBarPlaying = (BottomBar) this.findViewById(R.id.MainActivity_bottomBar);

        //设置点击处理事件
        PlayTouchUtil playTouchUtil = new PlayTouchUtil();
        bottomBarPlaying.setTouchHandle(playTouchUtil);


        //往viewPager的数据列表中添加2个碎片；
        fragmentList.add(new ArtistPageFragment());
        fragmentList.add(new SongsPageFragment());

        fragmentPagerAdapter = new MyPageAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bottomBarPlaying.setVisibilityChange(false);
        //second_bottomBar.setVisilityChange(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomBarPlaying !=null&& bottomBarPlaying.getIsPullUp())
            bottomBarPlaying.setVisibilityChange(true);
    }
}

