package com.waterteam.musicproject.viewpagers.artist.detial.songs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.adapter.AlbumSongsAdapter;

import com.waterteam.musicproject.bean.SongsBean;

import java.util.List;

/**
 * Created by BA on 2018/2/3 0003.
 *
 * @Function :艺术家详情界面的歌曲列表
 */

public class ArtistDetailSongsPageFragment extends Fragment{
    List<SongsBean> songs;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_detail_songs,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.artist_detail_album_song);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(new AlbumSongsAdapter(songs));
    }

        public void setData(List<SongsBean> songs){
        this.songs=songs;
    }
}
