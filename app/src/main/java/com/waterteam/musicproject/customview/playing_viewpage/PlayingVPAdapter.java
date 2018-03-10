package com.waterteam.musicproject.customview.playing_viewpage;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.waterteam.musicproject.customview.gravity_imageview.RotationCarView;

import java.util.List;

/**
 * Created by BA on 2018/3/9 0009.
 *
 * @Function :
 */

public class PlayingVPAdapter extends PagerAdapter {

    private List<RotationCarView> linearLayouts;

    //将从这里取出View
    public PlayingVPAdapter(List<RotationCarView> list) {
        this.linearLayouts = list;
    }

    //比如ViewPager一次会创建3个Pagr，当你在第一页的时候，ViewPagr会创建前3页，也就会自动调用这个方法将对应的View价加载进ViewPager中
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(linearLayouts.get(position));
        return linearLayouts.get(position);//返回当前布局
    }

    @Override
    public int getCount() {
        return linearLayouts.size();
    }

    //这个用来判断是不是从对象中获取View
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //当滑动到第3页时，第一页会被杀掉
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(linearLayouts.get(position));
    }
}

