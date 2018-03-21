package com.waterteam.musicproject.viewpagers.artist.detail.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.AlbumBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class ArtistAlbumFragment extends Fragment{

    private int position; //记录专辑在艺术家专辑数据的位置
    List<AlbumBean> albums ;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_fragment_artist_detail_album,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_artist_detail_albumRecycle);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(new ArtistAlbumsAdaperText(getActivity(),albums,position));
    }

    public void setData(List<AlbumBean> albums,int position){
        this.albums=albums;
        this.position=position;
    }

}
