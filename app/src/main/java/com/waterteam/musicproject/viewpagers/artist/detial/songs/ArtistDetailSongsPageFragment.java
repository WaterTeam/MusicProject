package com.waterteam.musicproject.viewpagers.artist.detial.songs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    AlbumSongsAdapter albumSongsAdapter;

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

        albumSongsAdapter= new AlbumSongsAdapter(songs);

        recyclerView.setAdapter(albumSongsAdapter);
    }

        public void setData(List<SongsBean> songs){
        this.songs=songs;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position=albumSongsAdapter.getLongPassPosition();
        switch (item.getItemId()){
            case AlbumSongsAdapter.NEXT_PLAY_ID:
                Toast.makeText(getContext(), "下一首播放", Toast.LENGTH_SHORT).show();
                return true;
            case AlbumSongsAdapter.ADD_TO_LIST_ID:
                Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                return true;
            case AlbumSongsAdapter.ALWAYS_PLAY_ID:
                Toast.makeText(getContext(), "单曲循环播放", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
