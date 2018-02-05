package com.waterteam.musicproject;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.ArtistBean;

import com.waterteam.musicproject.util.GetCoverUtil;
import com.waterteam.musicproject.util.HandleBottomBar;
import com.waterteam.musicproject.util.StatusBarUtil;
import com.waterteam.musicproject.viewpagers.MyPageAdapter;
import com.waterteam.musicproject.viewpagers.artist.detail.album.ArtistDetailAlbumPageFragment;
import com.waterteam.musicproject.viewpagers.artist.detial.songs.ArtistDetailSongsPageFragment;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ArtistDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ArtistDetailsActivity";
    private boolean debug = false;
    ImageView imageView;
    TextView textView;
    TextView artistDetail;
    ViewPager viewPager;
    ArtistBean artistBean;
    Button albumButton;
    Button songButton;
    int position; //记录艺术家的位置，用来取数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);

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

        //初始化控件
        imageView = (ImageView) findViewById(R.id.album_image);
        textView = (TextView) findViewById(R.id.album_name);
        artistDetail = (TextView) findViewById(R.id.artist_detail);

        //设置数据
        initData();
        initView();

        //设置监听
        setButtonOnClick();

        //启动动画
        startAnimator();

    }

    /**
     * 设置数据
     * @author BA on 2018/2/1 0001
     * @param
     * @return
     * @exception
     */
    private void initData(){
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        artistBean = AllMediaBean.getInstance().getArtists().get(position);
        if (artistBean != null) {
            textView.setText(artistBean.getName());
            artistDetail.setText("共有"+artistBean.getAlbumCount()+"张专辑和"+artistBean.getSongsCount()+"首歌曲");
            GetCoverUtil.setCover(this, artistBean, imageView, 400);
        }
    }

    /**
     * 专辑标题的动画
     * @author BA on 2018/2/1 0001
     * @param
     * @return
     * @exception
     */
    private void startAnimator(){
        textView.setTranslationX(-500f);
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(textView,"translationX",0);
        objectAnimator.setStartDelay(500);
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }

    /**
     * initView()方法用于初始化主活动界面
     *
     * @param
     * @return
     * @throws
     * @author BA on 2017/12/5.
     */
    private void initView() {

        viewPager = (ViewPager) findViewById(R.id.albums_and_songs);
        albumButton=(Button)findViewById(R.id.button_album);
        songButton=(Button)findViewById(R.id.button_song);
        songButton.setAlpha(0);

        List<Fragment> fragmentList=new ArrayList<>();

        ArtistDetailAlbumPageFragment artistDetailAlbumPageFragment= new ArtistDetailAlbumPageFragment();
        ArtistDetailSongsPageFragment artistDetailSongsPageFragment=
                new ArtistDetailSongsPageFragment();

        artistDetailSongsPageFragment.setData(artistBean.getSongs());
        artistDetailAlbumPageFragment.setData(artistBean.getAlubums(),position);

        //往viewPager的数据列表中添加碎片；
        fragmentList.add(artistDetailAlbumPageFragment);
        fragmentList.add(artistDetailSongsPageFragment);

        viewPager.setAdapter( new MyPageAdapter(getSupportFragmentManager(), fragmentList));

        //监听页数改变
        setOnPageChangeListener();
    }

    /**
     *  实现点击后切换ViewPage
     * @author BA on 2018/2/3 0003
     * @param
     * @return
     * @exception
     */
    private void setButtonOnClick(){
        songButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1,true);
            }
        });

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0,true);
            }
        });
    }

    /**
     * 实现滑动时按钮颜色渐变
     * @author BA on 2018/2/3 0003
     * @param
     * @return
     * @exception
     */
    private void setOnPageChangeListener(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset!=0){
                    songButton.setAlpha(positionOffset);
                    albumButton.setAlpha(1-positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    songButton.setAlpha(0);
                    albumButton.setAlpha(1);
                }else {
                    songButton.setAlpha(1);
                    albumButton.setAlpha(0);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //必须写该方法
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("datas", AllMediaBean.getInstance());
    }
}
