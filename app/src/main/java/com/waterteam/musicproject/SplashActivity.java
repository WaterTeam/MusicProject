package com.waterteam.musicproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.AlbumBean;
import com.waterteam.musicproject.bean.ArtistBean;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.util.getdatautil.GetAlbumUtil;
import com.waterteam.musicproject.util.getdatautil.GetArtistUtil;
import com.waterteam.musicproject.util.getdatautil.GetSongUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.waterteam.musicproject.viewpagers.songs.page.Cn2Spell;

/**
 * Created by BA on 2017/12/7 0007
 *
 * @Function : App启动页，在这里申请权限，还有进行全局的数据初始化操作
 */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    //权限请求码
    private final int REQUEST_PERMISSION_CODE = 843;
    private boolean songsOK = false;
    private boolean albumOK = false;
    private boolean artistOK = false;
    public boolean waitingASecond=false;

    private MyHandler myHandler; //用来打开MainActivity
    static class MyHandler extends Handler{
        private WeakReference<AppCompatActivity> weakReference;
        public MyHandler(AppCompatActivity activity){
            weakReference=new WeakReference<AppCompatActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AppCompatActivity activity = weakReference.get();
            if (msg.what==2) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, R.anim.anim_out);
                activity.finish();
            }else {
                ((SplashActivity)activity).waitingASecond=true;
                ((SplashActivity)activity).startActivity();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //检查权限
        boolean havePermission = checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (havePermission) {
            //初始化数据
            initData();
        }
    }

    /**
     * 异步加载数据
     *
     * @param
     * @return
     * @throws
     * @author BA on 2017/12/8 0008
     */
    private void initData() {
        myHandler=new MyHandler(this);
        //默认让启动页停一秒
        Message message=new Message();
        message.what=1;
        myHandler.sendMessageDelayed(message,1000);

        //初始化数据
        getSongs();
        getAlubms();
        getArtists();
    }


    /**
     * 获取歌曲
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/1/27 0027
     */
    private void getSongs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SongsBean> songs = new GetSongUtil().start(SplashActivity.
                        this, MediaStore.Audio.Media.DURATION + ">=? and " + MediaStore.Audio.Media.DURATION + "<=?", new String[]{"90000", "1200000"});

                //在这里获取firstLetter
                songsGetFirstLetter(songs);
                //再把歌曲按字母顺序排好序，如若在碎片中排序，则打开这个碎片页面要延迟大约1秒多
                Collections.sort(songs);
                AllMediaBean.getInstance().setSongs(songs);
                songsOK=true;
                startActivity();
            }
        }).start();
    }



    /**
     * 获取专辑
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/1/27 0027
     */
    private void getAlubms() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<AlbumBean> albums = new GetAlbumUtil().start(SplashActivity.this, null, null);
                AllMediaBean.getInstance().setAlbums(albums);
                albumOK=true;
                startActivity();
            }
        }).start();
    }

    /**
     * 获取艺术家
     *
     * @param
     * @return
     * @throws
     * @author BA on 2018/1/27 0027
     */
    private void getArtists() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ArtistBean> artists = new GetArtistUtil().start(SplashActivity.this, null, null);
                AllMediaBean.getInstance().setArtists(artists);
                artistOK=true;
                startActivity();
            }
        }).start();
    }

    private void startActivity(){
        synchronized (new Object()) {
            if (songsOK && albumOK && artistOK&&waitingASecond) {
                waitingASecond=false;
                Message message=new Message();
                message.what=2;
                myHandler.sendMessage(message);
            }
        }
    }



    /**
     * 获取歌曲名的首字母
     * @author BA on 2018/1/29 0029
     * @param
     * @return
     * @exception
     */
    private void songsGetFirstLetter(List<SongsBean> songList){
        for(SongsBean song:songList){
            String name = song.getName();
            if (name != null && Cn2Spell.getPinYin(name) != null && Cn2Spell.getPinYin(name).length() >= 1) {
                Log.d(TAG, "SongsBean: "+name);
                song.setFirstLetter( Cn2Spell.getPinYin(name).substring(0, 1).toUpperCase());
                if (song.getFirstLetter()!=null&&!song.getFirstLetter().matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
                    song.setFirstLetter("#");
                }
            } else {
                song.setFirstLetter("#");
            }
        }
    }

    /**
     * 检查是否有权限
     *
     * @param permissions 需要申请的权限
     * @return true：有权限，false：无权限
     * @throws
     * @author BA on 2017/12/7 0007.
     */
    private boolean checkPermission(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> mPermissions = new ArrayList<>();
            for (String p : permissions) {
                int result = ContextCompat.checkSelfPermission(this, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    mPermissions.add(p);
                }
            }

            if (mPermissions.size() != 0) {
                ActivityCompat.requestPermissions(this
                        , mPermissions.toArray(new String[mPermissions.size()])
                        , REQUEST_PERMISSION_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.length != 0) {
            for (int g : grantResults) {
                if (g != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "没有权限，程序无法正常运行", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            //初始化数据，然后启动主界面
            initData();
        } else {
            Toast.makeText(this, "没有权限，程序无法正常运行", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
