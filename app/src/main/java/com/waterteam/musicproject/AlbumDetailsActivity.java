package com.waterteam.musicproject;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.waterteam.musicproject.adapter.AlbumSongsAdapter;
import com.waterteam.musicproject.bean.AlbumBean;
import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.util.GetCoverUtil;
import com.waterteam.musicproject.util.StatusBarUtil;
import com.waterteam.musicproject.viewpagers.songs.page.SongsRVAdapter;

import java.util.List;

public class AlbumDetailsActivity extends AppCompatActivity {
    private static final String TAG = "AlbumDetailsActivity";
    RecyclerView recyclerView;
    ImageView imageView;
    TextView textView;
    TextView songsNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        //设置为沉浸式状态栏，设置了状态栏颜色及字体颜色
        StatusBarUtil.setStatusBarLightMode(this);

        //初始化控件
        recyclerView = (RecyclerView) findViewById(R.id.songs_rv);
        imageView = (ImageView) findViewById(R.id.album_image);
        textView = (TextView) findViewById(R.id.album_name);
        songsNum = (TextView) findViewById(R.id.songs_num);

        //设置数据
        initData();

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
        int position = intent.getIntExtra("position", 0);
        AlbumBean albumBean = AllMediaBean.getInstance().getAlbums().get(position);
        if (albumBean != null) {
            List<SongsBean> songs = albumBean.getSongs();
            textView.setText(albumBean.getName());
            songsNum.setText(songs.size() + "首歌曲");
            GetCoverUtil.setCover(this, albumBean, imageView, 400);

            recyclerView.setAdapter(new AlbumSongsAdapter(songs));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
}
