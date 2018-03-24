package com.waterteam.musicproject.customview.playing_viewpage;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.GetCoverUri;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.util.GetCoverUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BA on 2018/3/9 0009.
 *
 * @Function :
 */

public class PlayingVPAdapter extends PagerAdapter {
    private static final String TAG = "PlayingVPAdapter";

    private List<SongsBean> songs;
    private List<FrameLayout> layouts;

    //将从这里取出View
    public PlayingVPAdapter(List<SongsBean> songs) {
        this.songs = songs;
        layouts=new ArrayList<>();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    //比如ViewPager一次会创建3个Pagr，当你在第一页的时候，ViewPagr会创建前3页，也就会自动调用这个方法将对应的View价加载进ViewPager中
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //动态加载布局，并且设置图片封面
        FrameLayout layout=getLayout(container.getContext());
        layouts.add(layout);
//        setCover(container.getContext(),layout,songs.get(position));
        container.addView(layout);
        return layout;//返回当前布局
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    //这个用来判断是不是从对象中获取View
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //当滑动到第3页时，第一页会被杀掉
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(layouts.get(position));

    }

    //动态加载布局
    private FrameLayout getLayout( Context context){
        Log.d(TAG, "getLayout: ");

        FrameLayout view=(FrameLayout) LayoutInflater.
                from(context).inflate(R.layout.vp_playing_item,null,false);

        return view;
    }

    private void setCover(Context context,FrameLayout frameLayout,GetCoverUri bean){
        Log.d(TAG, "setCover: ");
        ImageView imageView=(ImageView)frameLayout.findViewById(R.id.play_image);
        GetCoverUtil.setCover(context,bean,imageView);
    }
}

