package com.waterteam.musicproject.bean;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.customview.gravity_imageview.RotationCarView;
import com.waterteam.musicproject.service.playmusic.service.PlayService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BA on 2018/3/9 0009.
 *
 * @Function :
 */

public class WaitingPlaySongsLayouts {
    private List<RotationCarView> list;
    private Context context;

    //传一个应用程序级别的Context
    public WaitingPlaySongsLayouts(Context context){
        list=new ArrayList<>();
        this.context=context;
    }

    public List<RotationCarView> getPlayingLayout() {
        return list;
    }

    public void addLayoutToFoot(SongsBean songsBean) {
        list.add(getLayout(songsBean));
    }


    public void removeSong(int position) {
        list.remove(position);
    }

    public void addSongToPosition(int position, SongsBean songsBean) {

        list.add(position, getLayout(songsBean));
    }

    public void removeAll() {
        list.clear();
    }

    public void addList(List<SongsBean> songs) {
        if (songs != null) {
            Log.d("viewviewview", "addList: ");
            list.clear();
            for (SongsBean song: PlayService.playList.getSongs()){
              list.add(getLayout(song));
            }
            Log.d("viewviewview", "addList: "+list.size());
        }
    }

    public int getSongsCount() {
        return list.size();
    }

    private RotationCarView getLayout(GetCoverUri bean){
        RotationCarView view=(RotationCarView) LayoutInflater.
                from(context).inflate(R.layout.buttombar_playing_img_vp_layout,null,false);
        view.setBean(bean);
        return view;
    }
}
