package com.waterteam.musicproject;

import android.animation.ObjectAnimator;
import android.content.Intent;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;

import android.view.animation.DecelerateInterpolator;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.bastatusbar.BAStatusBar;
import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.ArtistBean;

import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.eventsforeventbus.EventFromBar;
import com.waterteam.musicproject.eventsforeventbus.EventToBarFromService;
import com.waterteam.musicproject.service.playmusic.service.PlayService;
import com.waterteam.musicproject.util.GetCoverUtil;
import com.waterteam.musicproject.util.StatusBarUtil;
import com.waterteam.musicproject.viewpagers.MyPageAdapter;
import com.waterteam.musicproject.viewpagers.artist.detail.album.ArtistDetailAlbumPageFragment;
import com.waterteam.musicproject.viewpagers.artist.detial.songs.ArtistDetailSongsPageFragment;


import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ArtistDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ArtistDetailsActivity";
    private boolean debug = false;
    ImageView headImage;
    TextView textView;
    TextView artistDetail;
    ViewPager viewPager;
    ArtistBean artistBean;
    TabLayout tabLayout;
    int position; //记录艺术家的位置，用来取数据

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
        setContentView(R.layout.activity_artist_details);

        // 设置为沉浸式状态栏，设置了状态栏颜色及字体颜色
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

        //初始化控件
        headImage = (ImageView) findViewById(R.id.album_image);
        textView = (TextView) findViewById(R.id.album_name);
        artistDetail = (TextView) findViewById(R.id.artist_detail);

        //设置数据
        initData();
        initView();

        //启动动画
        startAnimator();


    }

    /**
     * 设置数据
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/1 0001
     */
    private void initData() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        artistBean = AllMediaBean.getInstance().getArtists().get(position);
        if (artistBean != null) {
            textView.setText(artistBean.getName());
            artistDetail.setText("共有" + artistBean.getAlbumCount() + "张专辑和" + artistBean.getSongsCount() + "首歌曲");
            GetCoverUtil.setCover(this, artistBean, headImage, 400);
        }
    }

    /**
     * 专辑标题的动画
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/1 0001
     */
    private void startAnimator() {
        textView.setTranslationX(-500f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(textView, "translationX", 0);
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


        List<Fragment> fragmentList = new ArrayList<>();

        ArtistDetailAlbumPageFragment artistDetailAlbumPageFragment = new ArtistDetailAlbumPageFragment();
        ArtistDetailSongsPageFragment artistDetailSongsPageFragment =
                new ArtistDetailSongsPageFragment();

        artistDetailSongsPageFragment.setData(artistBean.getSongs());
        artistDetailAlbumPageFragment.setData(artistBean.getAlubums(), position);

        //往viewPager的数据列表中添加碎片；
        fragmentList.add(artistDetailAlbumPageFragment);
        fragmentList.add(artistDetailSongsPageFragment);

        List<String> titles = new ArrayList<>();
        titles.add("专辑");
        titles.add("歌曲");

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager(), fragmentList, titles));

        reflex(tabLayout);
    }

    //必须写该方法
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("datas", AllMediaBean.getInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 设置Tab下划线
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/8 0008
     */
    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

//                    int dp10 = dip2px(tabLayout.getContext(), 10);

                    int dp10 = 200;

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
