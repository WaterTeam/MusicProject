package com.waterteam.musicproject.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.commit451.nativestackblur.NativeStackBlur;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.customview.BottomBar;
import com.waterteam.musicproject.customview.BottomBarTouchListener;
import com.waterteam.musicproject.customview.gravity_imageview.MySensorObserver;
import com.waterteam.musicproject.customview.gravity_imageview.RotationCarView;
import com.waterteam.musicproject.customview.playing_viewpage.DepthPageTransformer;
import com.waterteam.musicproject.customview.playing_viewpage.PlayingVPAdapter;
import com.waterteam.musicproject.eventsforeventbus.EventFromBar;
import com.waterteam.musicproject.eventsforeventbus.EventFromTouch;
import com.waterteam.musicproject.eventsforeventbus.EventToBarFromService;
import com.waterteam.musicproject.service.playmusic.service.PlayService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BA on 2018/2/6 0006.
 *
 * @Function : 处理第一个BottomBar的点击事件，注意，只发送对应的事件statu，进度条就需要传进度,由于需要用到第二个bottomBar的一个方法，所以在外面初始化时就要传一个第二个bottomBar的事件处理类handleSecondBottomBarUtility
 * 不用传歌曲位置
 */

public class HandleBottomBarTouchUtil implements BottomBarTouchListener {
    private static final String TAG = "HandleBottomBarTouchUti";

    private TextView bottomBar_songName;
    private TextView bottomBar_singer;
    private Button bottomBar_playButton;
    private ImageView bottomBar_image;
    private TextView bottomBar_palying_songs_name;//播放界面中的歌曲名

    private Context context;

    HandleSecondBottomBarUtil util;

    private ImageView frostedGlassImage;

    private MySensorObserver sensorObserver;

    private ViewPager viewPager;
    private PlayingVPAdapter playingVPAdapter;

    private View bottomBar;
    private View bottomContent;

    private static boolean isPlaying = false;
    private static final int LISTPLAY = 0;

    private boolean isUserMoveViewPager = true;


    @Override
    public void setContentView(final BottomBar view) {
        this.bottomBar = view.bottomBar;
        this.bottomContent = view.bottomContent;
        this.context = view.getContext();
        initGravityImageView();
        view.setVisibilityListener(new BottomBar.VisibilityListener() {
            @Override
            public void statusChange(boolean isUp) {
                if (isUp) {
                    Log.d(TAG, "statusChange: 注册");
                    sensorObserver.register(view.getContext());
                } else {
                    sensorObserver.unregister();
                    Log.d(TAG, "statusChange: 取消注册");
                }
            }
        });
        findView();
        handleClick();
        flashBottomBar();
        EventBus.getDefault().register(this);
    }

    private void initGravityImageView() {
        bottomBar_image = (ImageView) bottomContent.findViewById(R.id.play_image);


        sensorObserver = new MySensorObserver();
        sensorObserver.setMaxRotateRadian(Math.PI / 10);

//        view.setGyroscopeObserver(sensorObserver);

    }

    private void findView() {
        viewPager = (ViewPager) bottomContent.findViewById(R.id.view_page);
        bottomBar_songName = (TextView) bottomBar.findViewById(R.id.bottomBar_songName);
        bottomBar_singer = (TextView) bottomBar.findViewById(R.id.bottomBar_singer);
        bottomBar_playButton = (Button) bottomBar.findViewById(R.id.bottomBar_play_button);
        bottomBar_palying_songs_name = (TextView) bottomContent.findViewById(R.id.playing_song_name);
        frostedGlassImage = (ImageView) bottomContent.findViewById(R.id.frosted_glass_image);
    }


    private void handleClick() {
        bottomBar_playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFromBar eventFromBar = new EventFromBar();
                if (isPlaying) {
                    eventFromBar.setStatu(EventFromBar.PAUSE);
                    EventBus.getDefault().post(eventFromBar);
                    isPlaying = false;
                } else {
                    eventFromBar.setStatu(EventFromBar.PAUSETOPLAY);
                    EventBus.getDefault().post(eventFromBar);
                    isPlaying = true;
                }
            }
        });
    }

    /**
     * 初始化VIewPage
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/3/9 0009
     */
    public void setViewPager() {
        if (PlayService.waitingPlaySongsLayouts.getSongsCount() <= 0) {
            PlayService.waitingPlaySongsLayouts.addList(AllMediaBean.getInstance().getWaitingPlaySongs().getSongs());
        }
        playingVPAdapter = new PlayingVPAdapter(PlayService.waitingPlaySongsLayouts.getPlayingLayout());
        viewPager.setAdapter(playingVPAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        //setViewPagerListener();
        Log.d("viewviewview", "setViewPager: " + PlayService.waitingPlaySongsLayouts.getPlayingLayout().size());
    }

    /**
     * 滑动时切歌的监听
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/3/9 0009
     */

    public void setViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (isUserMoveViewPager) {
                   PlayService.position = position;
                    EventFromBar eventFromBar = new EventFromBar();
                    eventFromBar.setStatu(EventFromBar.VIEWPAGERMOVE);
                    EventBus.getDefault().post(eventFromBar);
                    new DownloadTask().execute();
                } else {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    class DownloadTask extends AsyncTask<Void,Integer,Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            publishProgress(PlayService.position);
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            isPlaying = true;
            SongsBean song = PlayService.playList.getSongs().get(values[0]);
            bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
            bottomBar_palying_songs_name.setText(song.getName());
            bottomBar_songName.setText(song.getName());
            bottomBar_singer.setText(song.getAuthor());
            setCover(song);
            if(util != null){
                util.changeViewForViewPager();
            }
            super.onProgressUpdate(values);

        }
    }


    /**
     * 切歌的时候用来切换ViewPage的位置
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/3/9 0009
     */
    public void setViewPagePosition(int position) {
        isUserMoveViewPager = false;
        viewPager.setCurrentItem(position, true);
        isUserMoveViewPager = true;
    }

    /**
     * 同步BottomBar
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/26 0026
     */
    private void flashBottomBar() {
        Log.d(TAG, "flashBottomBar: ");
        SongsBean songsBean = PlayService.NowPlaySong;
        if (songsBean != null) {
            if (PlayService.isPlay) {
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
            } else {
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_play_button);
            }
            bottomBar_songName.setText(songsBean.getName());
            bottomBar_singer.setText(songsBean.getAuthor());
            bottomBar_palying_songs_name.setText(songsBean.getName());
            setCover(songsBean);
        }

        setViewPager();
        setViewPagePosition(PlayService.position);
        setViewPagerListener();

    }

    @Subscribe
    public void changBottmBarView(EventToBarFromService event) {
        switch (event.getStatu()) {
            case EventToBarFromService.PAUSE: {
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_play_button);
                //seekBar要设置停住
                isPlaying = false;
            }
            break;
            case EventToBarFromService.PAUSETOPLAY: {
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                isPlaying = true;
            }
            break;
            case EventToBarFromService.PLAYANEW: {
                isPlaying = true;
                SongsBean song = event.getSongsBeanList().get(event.getPosition());
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_palying_songs_name.setText(song.getName());
                bottomBar_songName.setText(song.getName());
                bottomBar_singer.setText(song.getAuthor());
                setViewPagePosition(event.getPosition());
                setCover(song);
                Log.e("MainActivity", "执行一次");
            }
            break;
            /*case EventToBarFromService.MOVEVIEWPAGER:
                isPlaying = true;
                SongsBean song = PlayService.playList.getSongs().get(event.getPosition());
                bottomBar_playButton.setBackgroundResource(R.drawable.ic_bottombar_pause_button);
                bottomBar_palying_songs_name.setText(song.getName());
                bottomBar_songName.setText(song.getName());
                bottomBar_singer.setText(song.getAuthor());
                setCover(song);
                break;*/
            default:
                break;
        }
    }

    /**
     * 变色
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/2/14 0014
     */
    private void setCover(SongsBean song) {
        new GetCoverUtil().setOnCompletedListener(new GetCoverUtil.CompletedLoadListener() {
            @Override
            public void completed(Bitmap bitmap) {
                Log.d(TAG, "completed: " + bitmap);
//                PaletteUtil paletteUtil = new PaletteUtil();
//                paletteUtil.from(bitmap).to(play_control_layout);
//                paletteUtil.from(bitmap).to(bottomBar_head_layout);
                Bitmap bm = ImageEffect(bitmap, 0, 0, 0.7f);
                //半径越大，处理后的图片越模糊
                bm = NativeStackBlur.process(bm, 3);
                frostedGlassImage.setImageBitmap(bm);
            }
        }).getCoverAsBitmap(bottomBar.getContext(), song, 20, 20);

        new GetCoverUtil().setOnCompletedListener(new GetCoverUtil.CompletedLoadListener() {
            @Override
            public void completed(Bitmap bitmap) {
                Log.d(TAG, "completed: " + bitmap);
//                PaletteUtil paletteUtil = new PaletteUtil();
//                paletteUtil.from(bitmap).to(play_control_layout);
//                paletteUtil.from(bitmap).to(bottomBar_head_layout);
                //半径越大，处理后的图片越模糊

                //  bottomBar_image.setImageBitmap(bitmap);
            }
        }).getCoverAsBitmap(bottomBar.getContext(), song, 400, 400);
    }

    /**
     * @param bm
     * @param hue        色相
     * @param saturation 饱和度
     * @param lum        亮度
     * @return
     */
    public Bitmap ImageEffect(Bitmap bm, float hue, float saturation, float lum) {

        Bitmap bitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

//        //色相调节
//        ColorMatrix hueMatrix = new ColorMatrix();
//        hueMatrix.setRotate(0, hue);
//        hueMatrix.setRotate(1, hue);
//        hueMatrix.setRotate(2, hue);
//
//        //饱和度调节
//        ColorMatrix saturationColorMatrix = new ColorMatrix();
//        saturationColorMatrix.setSaturation(saturation);

        //亮度调节
        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(lum, lum, lum, 1);

        ColorMatrix ImageMatrix = new ColorMatrix();
//        ImageMatrix.postConcat(hueMatrix);
//        ImageMatrix.postConcat(saturationColorMatrix);
        ImageMatrix.postConcat(lumMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(ImageMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);

        return bitmap;
    }
    public void setHandleSecondBarUtil(HandleSecondBottomBarUtil util){
        this.util = util;
    }
}