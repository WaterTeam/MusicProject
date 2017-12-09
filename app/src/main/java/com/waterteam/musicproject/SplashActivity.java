package com.waterteam.musicproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.waterteam.musicproject.util.get_data_util.GetAlbumUtil;
import com.waterteam.musicproject.util.get_data_util.GetArtistUtil;
import com.waterteam.musicproject.util.get_data_util.GetSongUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BA on 2017/12/7 0007
 *
 * @Function : App启动页，在这里申请权限，还有进行全局的数据初始化操作
 */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    //权限请求码
    private final int REQUEST_PERMISSION_CODE = 843;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d(TAG, "onCreate: ");
        EventBus.getDefault().register(this);
        //检查权限
        boolean havePermission = checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE
        ,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (havePermission) {
            //初始化数据
            initData();
        }
    }

    /**
     * 加载数据成功，去主界面
     *
     * @param m 成功后发送的信息，用来做标识
     * @return
     * @throws
     * @author BA on 2017/12/8 0008
     */
    @Subscribe
    public void initDataSuccess(String m) {
        if ("initDataSuccess".equals(m)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                //初始化数据
                List<SongsBean> songs = new GetSongUtil().start(SplashActivity.this, null, null);
                List<AlbumBean> albums = new GetAlbumUtil().start(SplashActivity.this, null, null);
                List<ArtistBean> artists = new GetArtistUtil().start(SplashActivity.this, null, null);

                //将数据全局保存
                AllMediaBean mySongsData = AllMediaBean.getInstance();
                mySongsData.setArtists(artists);
                mySongsData.setAlbums(albums);
                mySongsData.setSongs(songs);

                EventBus.getDefault().post("initDataSuccess");
            }
        }).start();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
