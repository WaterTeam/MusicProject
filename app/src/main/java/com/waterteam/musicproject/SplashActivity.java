package com.waterteam.musicproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BA on 2017/12/7 0007
 * @Function : App启动页，在这里申请权限，还有进行全局的数据初始化操作
 */
public class SplashActivity extends AppCompatActivity {
    //权限请求码
    private final int REQUEST_PERMISSION_CODE=843;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //检查权限
        boolean havePermission=checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (havePermission) {
            //初始化数据，然后启动主界面
            Toast.makeText(this, "初始化数据然后启动主界面", Toast.LENGTH_SHORT).show();

//            //我要测试所以这里就不跳转
//            Intent intent=new Intent(this,MainActivity.class);
//            startActivity(intent);
        }
    }




    /**
     * 检查是否有权限
     * @author BA on 2017/12/7 0007.
     * @param 需要申请的权限
     * @return true：有权限，false：无权限
     * @exception
     */
    private boolean checkPermission(String...permissions){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
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
                        ,REQUEST_PERMISSION_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_PERMISSION_CODE&&grantResults.length!=0) {
            for (int g : grantResults) {
                if (g != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "没有权限，程序无法正常运行", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            //初始化数据，然后启动主界面
            Toast.makeText(this, "初始化数据然后启动主界面", Toast.LENGTH_SHORT).show();

//            //我要测试所以这里就不跳转
//            Intent intent=new Intent(this,MainActivity.class);
//            startActivity(intent);
        }else{
            Toast.makeText(this, "没有权限，程序无法正常运行", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
