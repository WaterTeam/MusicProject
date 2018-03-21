package com.waterteam.musicproject.util.bottombarutil;

import android.support.v7.widget.LinearLayoutManager;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.adapter.SecondBottomAdapter;
import com.waterteam.musicproject.bean.SongsBean;

import com.waterteam.musicproject.customview.MyRecycleView;

import com.waterteam.musicproject.customview.bottom.bar.BottomBar;
import com.waterteam.musicproject.customview.bottom.bar.BottomBarHandle;
import com.waterteam.musicproject.customview.bottom.bar.BottomBarPlaying;
import com.waterteam.musicproject.customview.bottom.bar.OnScrollerListener;
import com.waterteam.musicproject.eventsforeventbus.EventFromBar;
import com.waterteam.musicproject.eventsforeventbus.EventToBarFromService;
import com.waterteam.musicproject.service.playmusic.service.PlayService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 处理第二个BottomBar的工具类
 * Created by CNTon 2018/2/28.
 */

public class ControlTouchUtil implements BottomBarHandle {
    private static final String TAG = "HandleBottomBarTouchUti";

    private Button bottomBar_playingLayout_button;//播放界面中的播放按钮
    private TextView bottomBar_playing_song_length;
    private TextView bottomBar_now_play_time;
    private Button bottomBar_playing_nextSong;
    private Button bottomBar_playing_lastSong;
    private Button play_mode;
    private Button up_arrow;
    private SeekBar seekBar;

    private View bottomBar;
    private View bottomContent;

    private BottomBar bottomBarSecond;

    private MyRecycleView recyclerView;
    private TextView playlistCount;

    private static boolean isPlaying = false;
    private static final int LISTPLAY = 0;
    private static final int ALWAYSPLAY = 1;
    private static final int RANDOMPLAY = 2;
    private static int playMode = LISTPLAY;
    private PlayTouchUtil handle;


    @Override
    public void setContentView(final BottomBar view) {
        //这里为了拿到BottomBarPlaying；
        BottomBarPlaying bottomBarPlaying = (BottomBarPlaying) view.getParent().getParent();
        handle = (PlayTouchUtil) bottomBarPlaying.getTouchHandle();
        bottomBarSecond = view;
        this.bottomBar = view.bottomBarHead;
        this.bottomContent = view.bottomBarContent;
        findView();
        handleClick();
        flashBottomBar();
        setArrowAnimator(view);
        EventBus.getDefault().register(this);

    }


    private void findView() {
        bottomBar_playingLayout_button = (Button) bottomBar.findViewById(R.id.play_or_pause_button);
        bottomBar_playing_song_length = (TextView) bottomBar.findViewById(R.id.playing_song_length);
        bottomBar_playing_nextSong = (Button) bottomBar.findViewById(R.id.next_song_button);
        bottomBar_playing_lastSong = (Button) bottomBar.findViewById(R.id.last_song_button);
        bottomBar_now_play_time = (TextView) bottomBar.findViewById(R.id.play_progress);
        play_mode = (Button) bottomBar.findViewById(R.id.play_mode);
        seekBar = (SeekBar) bottomBar.findViewById(R.id.seekbar);
        up_arrow = (Button) bottomBar.findViewById(R.id.up_arrow);
        //bottomBarSecond.isSecond =  true;
        //up_arrow.setSecondBottom(bottomBarSecond);
        recyclerView = (MyRecycleView) bottomContent.findViewById(R.id.second_bottomBar_recycleView);
        recyclerView.setBottomBar(bottomBarSecond);
        playlistCount = (TextView) bottomContent.findViewById(R.id.play_list_count);
    }


    private void handleClick() {
        bottomBar_playingLayout_button.setOnClickListener(new View.OnClickListener() {
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
        bottomBar_playing_nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (PlayService.position < PlayService.playList.getSongsCount()-1) {
                        //handle.setViewPagePosition(PlayService.position + 1);
                        EventFromBar eventFromBar = new EventFromBar();
                        eventFromBar.setStatu(EventFromBar.PLAYNEXT);
                        EventBus.getDefault().post(eventFromBar);
                        isPlaying = true;
                    }
            }
        });
        bottomBar_playing_lastSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (PlayService.position > 0) {
                        EventFromBar eventFromBar = new EventFromBar();
                        eventFromBar.setStatu(EventFromBar.PLAYLAST);
                        EventBus.getDefault().post(eventFromBar);
                        isPlaying = true;

                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bottomBar_now_play_time.setText(formatTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //seekBar.setProgress(seekBar.get);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EventFromBar eventFromBar = new EventFromBar();
                eventFromBar.setStatu(EventFromBar.SEEKBARMOVE);
                eventFromBar.setProgress(seekBar.getProgress());
                EventBus.getDefault().post(eventFromBar);
            }
        });
        play_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFromBar eventFromBar = new EventFromBar();
                playMode = (playMode + 1) % 3;
                switch (playMode) {
                    case LISTPLAY:
                        eventFromBar.setStatu(EventFromBar.LISTMODE);
                        EventBus.getDefault().post(eventFromBar);
                        play_mode.setBackgroundResource(R.drawable.ic_list_play_button);
                        Toast toast = Toast.makeText(bottomBar.getContext(), "列表循环", Toast.LENGTH_LONG);
                        showMyToast(toast, 500);
                        break;
                    case ALWAYSPLAY:
                        eventFromBar.setStatu(EventFromBar.SIMPLEMODE);
                        EventBus.getDefault().post(eventFromBar);
                        play_mode.setBackgroundResource(R.drawable.ic_single_play_button);
                        Toast toast1 = Toast.makeText(bottomBar.getContext(), "单曲循环", Toast.LENGTH_LONG);
                        showMyToast(toast1, 500);
                        break;
                    case RANDOMPLAY:
                        eventFromBar.setStatu(EventFromBar.RANDOMMODE);
                        EventBus.getDefault().post(eventFromBar);
                        play_mode.setBackgroundResource(R.drawable.ic_random_play_button);
                        Toast toast2 = Toast.makeText(bottomBar.getContext(), "随机播放", Toast.LENGTH_LONG);
                        showMyToast(toast2, 500);
                        break;
                    default:
                        break;
                }
            }
        });

        up_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bottomBarSecond.getIsPullUp()) {
                    bottomBarSecond.pullUp();
                } else {
                    bottomBarSecond.pullDown();//第二个bottomBar下拉后不应该去影响状态栏，状态栏由第一个bottomBar决定
                    //StatusBarUtil.setStatusBarDarkMode((Activity) bottomBarSecond.getContext());
                }
            }
        });


    }

    public void setArrowAnimator(BottomBar bar){
        bar.setScrollerListener(new OnScrollerListener() {
            @Override
            public void upDate(float progress) {
                Log.e(TAG, "upDate: "+progress );
                up_arrow.setRotation(180*progress);
            }
        });

    }

    private void flashBottomBar() {
        Log.d(TAG, "flashBottomBar: ");
        SongsBean songsBean = PlayService.NowPlaySong;
        if (songsBean != null) {
            if (PlayService.isPlay) {
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_pause_button);
            }
            bottomBar_playing_song_length.setText(songsBean.getFormatLenght());

            seekBar.setMax(songsBean.getLength());

            switch (PlayService.playMode) {
                case EventFromBar.LISTMODE:
                    play_mode.setBackgroundResource(R.drawable.ic_list_play_button);
                    break;
                case EventFromBar.SIMPLEMODE:
                    play_mode.setBackgroundResource(R.drawable.ic_single_play_button);
                    break;
                case EventFromBar.RANDOMMODE:
                    play_mode.setBackgroundResource(R.drawable.ic_random_play_button);
                    break;
                default:
                    break;
            }
            SecondBottomAdapter secondBottomAdapter = new SecondBottomAdapter(PlayService.playList.getSongs());
            recyclerView.setAdapter(secondBottomAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(bottomContent.getContext());
            layoutManager.scrollToPositionWithOffset(PlayService.position, 0);
            //layoutManager.setStackFrom(true);
            recyclerView.setLayoutManager(layoutManager);
            playlistCount.setText("  播放列表（" + (PlayService.position + 1) + ":" + PlayService.playList.getSongs().size() + "）");
        }

    }

    @Subscribe
    public void changSecondBottomBar(EventToBarFromService event) {
        switch (event.getStatu()) {
            case EventToBarFromService.PAUSE: {
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_play_button);
                //seekBar要设置停住
                isPlaying = false;
            }
            break;
            case EventToBarFromService.PAUSETOPLAY: {
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_pause_button);
                isPlaying = true;
            }
            break;
            case EventToBarFromService.PLAYANEW: {

                if (event.getPlayMode() != -1) {
                    switch (event.getPlayMode()) {
                        case LISTPLAY:
                            play_mode.setBackgroundResource(R.drawable.ic_list_play_button);
                            break;
                        case ALWAYSPLAY:
                            play_mode.setBackgroundResource(R.drawable.ic_single_play_button);
                            break;
                        case RANDOMPLAY:
                            play_mode.setBackgroundResource(R.drawable.ic_random_play_button);
                            break;
                        default:
                            break;
                    }
                    playMode = event.getPlayMode();
                }
                isPlaying = true;
                //SongsBean song = event.getSongsBeanList().get(event.getPosition());
                SongsBean song = PlayService.playList.getSongs().get(PlayService.position);
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_pause_button);
                bottomBar_playing_song_length.setText(song.getFormatLenght());
                seekBar.setProgress(0);
                seekBar.setMax(song.getLength());
                if (PlayService.playList.getSongs().size() > 5) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(bottomContent.getContext());
                    layoutManager.scrollToPositionWithOffset(PlayService.position, 0);
                    layoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(layoutManager);
                }

                playlistCount.setText("  播放列表（" + (PlayService.position + 1) + ":" + PlayService.playList.getSongs().size() + "）");
                Log.e("MainActivity", "执行一次");
            }

            break;
            case EventToBarFromService.MOVESEEKBAR: {
                bottomBar_now_play_time.setText(formatTime(event.getProgress()));
                seekBar.setProgress(event.getProgress());
            }
            break;
            case EventToBarFromService.SEEKBARMOVEITSELF: {
                bottomBar_now_play_time.setText(formatTime(event.getProgress()));
                seekBar.setMax(event.getSongsBeanList().get(event.getPosition()).getLength());
                seekBar.setProgress(event.getProgress());
            }
            break;
            case EventToBarFromService.MOVEVIewPAGER:
                if (event.getPlayMode() != -1) {
                    switch (event.getPlayMode()) {
                        case LISTPLAY:
                            play_mode.setBackgroundResource(R.drawable.ic_list_play_button);
                            break;
                        case ALWAYSPLAY:
                            play_mode.setBackgroundResource(R.drawable.ic_single_play_button);
                            break;
                        case RANDOMPLAY:
                            play_mode.setBackgroundResource(R.drawable.ic_random_play_button);
                            break;
                        default:
                            break;
                    }
                    playMode = event.getPlayMode();
                }
                isPlaying = true;
                SongsBean song = PlayService.playList.getSongs().get(event.getPosition());
                bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_pause_button);
                bottomBar_playing_song_length.setText(song.getFormatLenght());
                seekBar.setProgress(0);
                seekBar.setMax(song.getLength());
                if (PlayService.playList.getSongs().size() > 5) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(bottomBarSecond.getContext());
                    layoutManager.scrollToPositionWithOffset(PlayService.position, 0);
                    layoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(layoutManager);
                }
                playlistCount.setText("  播放列表（" + (PlayService.position + 1) + ":" + PlayService.playList.getSongs().size() + "）");
                Log.e("MainActivity", "执行一次");
                break;
            default:
                break;
        }
    }

    public void changeViewForViewPager() {
        isPlaying = true;
        SongsBean song = PlayService.playList.getSongs().get(PlayService.position);
        bottomBar_playingLayout_button.setBackgroundResource(R.drawable.ic_pause_button);
        bottomBar_playing_song_length.setText(song.getFormatLenght());
        seekBar.setProgress(0);
        seekBar.setMax(song.getLength());
        if (PlayService.position >= 2 && PlayService.playList.getSongs().size() > 5) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(bottomBarSecond.getContext());
            layoutManager.scrollToPositionWithOffset(PlayService.position, 0);
            layoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(layoutManager);
        }

        playlistCount.setText("  播放列表（" + (PlayService.position + 1) + ":" + PlayService.playList.getSongs().size() + "）");
    }

    private String formatTime(int Leng) {
        //格式化时长
        float fLength = Leng / 1000.00f / 60.00f;
        fLength = (float) Math.round(fLength * 100);
        fLength = fLength / 100;
        String sLength = fLength + "";

        String[] time = (sLength).split("\\.");
        String min = "";
        String second = "";
        if (time.length >= 1) min = time[0];
        if (time.length >= 2) second = time[1];
        if (second.length() < 2) {
            second += "0";
        }
        return min + ":" + second;
    }

    /**
     * 自定义Toast的显示时间
     *
     * @param
     * @return
     * @throws
     * @author CNT on 2018/2/10.
     */
    public void showMyToast(final Toast toast, final int cnt) {//设置了Toast显示的时间
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3500);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);
    }
}