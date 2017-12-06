package com.waterteam.musicproject;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import CustomControl.MyToolbar;
import ViewPagers_.AlbumPage.AlbumPageFragment;
import ViewPagers_.ArtistPage.ArtistPageFragment;
import ViewPagers_.MyPageAdapter;
import ViewPagers_.SongsPage.SongsPageFragment;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    List<Fragment> fragmentList  = new ArrayList<Fragment>();
    MyToolbar toolbar;
    MyPageAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (MyToolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        initView();

    }
    /**
     * initView()方法用于初始化主活动界面
     * @author CNT on 2017/12/5.
     * @param
     * @return
     * @exception
     */
    private void initView(){
        viewPager = (ViewPager) this.findViewById(R.id.viewPager_MainActivity);

        //往viewPager的数据列表中添加3个碎片；
        fragmentList.add(new ArtistPageFragment());
        fragmentList.add(new AlbumPageFragment());
        fragmentList.add(new SongsPageFragment());

        fragmentPagerAdapter = new MyPageAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(fragmentPagerAdapter);
    }
}
