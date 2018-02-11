package com.waterteam.musicproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterteam.musicproject.adapter.AlbumSongsAdapter;
import com.waterteam.musicproject.bean.AlbumBean;
import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.ArtistBean;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.eventsforeventbus.EventFromTouch;
import com.waterteam.musicproject.eventsforeventbus.EventToBarFromService;
import com.waterteam.musicproject.service.playmusic.service.PlayService;
import com.waterteam.musicproject.util.GetCoverUtil;
import com.waterteam.musicproject.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static java.security.AccessController.getContext;

public class AlbumDetailsActivity extends AppCompatActivity {
    private static final String TAG = "AlbumDetailsActivity";
    private boolean debug = false;
    ImageView albumCover;
    TextView albumName;
    TextView artistName;
    TextView songsCount;
    RecyclerView recyclerView;

    AlbumBean albumBean;

    AlbumSongsAdapter albumSongsAdapter;

    private TextView bottomBar_songName;
    private TextView bottomBar_singer;
    private Button bottomBar_playButton;
    private ImageView bottomBar_image;
    private Button bottomBar_playingLayout_button;//播放界面中的播放按钮
    private TextView bottomBar_palying_songs_name;//播放界面中的歌曲名
    private TextView bottomBar_playing_song_length;
    private TextView bottomBar_now_play_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

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

        albumCover = (ImageView) findViewById(R.id.album_image);
        albumName = (TextView) findViewById(R.id.album_name);
        artistName = (TextView) findViewById(R.id.artist_name);
        songsCount = (TextView) findViewById(R.id.songs_count);
        recyclerView = (RecyclerView) findViewById(R.id.album_detail_songs);

        initData();

        initBottomBar();
        handleBottomBar();
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
        int artistPosition = intent.getIntExtra("artistPosition", 0);
        int albumPosition = intent.getIntExtra("albumPosition", 0);
        albumBean = AllMediaBean.getInstance().getArtists().get(artistPosition).getAlubums().get(albumPosition);
        if (albumBean != null) {
            albumName.setText("专辑名: " + albumBean.getName());
            artistName.setText("艺术家: " + albumBean.getArtist());
            songsCount.setText("歌曲数: " + albumBean.getSongsCount() + "首歌曲");
            GetCoverUtil.setCover(this, albumBean, albumCover, 200);

            albumSongsAdapter = new AlbumSongsAdapter(albumBean.getSongs());
            LinearLayoutManager manager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(albumSongsAdapter);
        }
    }

    //必须写该方法
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("datas", AllMediaBean.getInstance());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = albumSongsAdapter.getLongPassPosition();
        EventFromTouch eventFromTouch = new EventFromTouch();
        switch (item.getItemId()) {
            case AlbumSongsAdapter.NEXT_PLAY_ID:
                eventFromTouch.setSong(albumBean.getSongs().get(position));
                eventFromTouch.setSongs(albumBean.getSongs());
                eventFromTouch.setStatu(EventFromTouch.ADD_TO_NEXT);
                EventBus.getDefault().post(eventFromTouch);
                Toast.makeText(this,"下一首播放", Toast.LENGTH_SHORT).show();
                return true;
            case AlbumSongsAdapter.ADD_TO_LIST_ID:
                eventFromTouch.setSong(albumBean.getSongs().get(position));
                eventFromTouch.setSongs(albumBean.getSongs());
                eventFromTouch.setStatu(EventFromTouch.ADD_TO_LIST);
                EventBus.getDefault().post(eventFromTouch);
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                return true;
            case AlbumSongsAdapter.ALWAYS_PLAY_ID:
                eventFromTouch.setSong(albumBean.getSongs().get(position));
                eventFromTouch.setSongs(albumBean.getSongs());
                eventFromTouch.setStatu(EventFromTouch.ALWAYS_PLAY);
                EventBus.getDefault().post(eventFromTouch);
                Toast.makeText(this, "单曲循环播放", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    private void initBottomBar(){
        bottomBar_songName = (TextView) findViewById(R.id.bottomBar_songName);
        bottomBar_singer = (TextView) findViewById(R.id.bottomBar_singer);
        bottomBar_playButton = (Button) findViewById(R.id.bottomBar_play_button);
        bottomBar_image = (ImageView) findViewById(R.id.play_image);
        bottomBar_playingLayout_button = (Button) findViewById(R.id.play_button);
        bottomBar_palying_songs_name = (TextView) findViewById(R.id.palying_songs_name);
        bottomBar_playing_song_length = (TextView) findViewById(R.id.palying_song_length);
    }
    private void handleBottomBar(){
        SongsBean songsBean = PlayService.NowPlaySong;
        if(PlayService.isPlay){
//            EventToBarFromService event = new EventToBarFromService();
//            event.setStatu(EventToBarFromService.PAUSETOPLAY);
//            EventBus.getDefault().post(event);
            bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_pause_button);
            bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
        }
        bottomBar_songName.setText(songsBean.getName());
        bottomBar_singer.setText(songsBean.getAuthor());
        bottomBar_palying_songs_name.setText(songsBean.getName());
        GetCoverUtil.setCover(this, songsBean, bottomBar_image, 600);
    }
}
