package com.waterteam.musicproject.viewpagers.artist.detail.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.AlbumBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

public class ArtistDetailAlbumPageFragment extends Fragment {

    private static final String TAG = "ArtistDetailAlbumPageFragment";

    private int position; //记录专辑在艺术家专辑数据的位置
    List<AlbumBean> albums ;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_detail_album,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.artist_detail_album_song);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GridLayoutManager manager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(new ArtistAlbumsAdapter(getActivity(),albums,position));
    }

    public void setData(List<AlbumBean> albums,int position){
        this.albums=albums;
        this.position=position;
    }
}
