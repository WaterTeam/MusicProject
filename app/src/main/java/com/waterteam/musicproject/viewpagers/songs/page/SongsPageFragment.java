package com.waterteam.musicproject.viewpagers.songs.page;

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
import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.SongsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CNT on 2017/12/6.
 */

public class SongsPageFragment extends Fragment {
    List<SongsBean> songsRV_dataList = new ArrayList<>();
    RecyclerView recyclerView;
    SongsRVAdapter songsRV_adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs_page, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.songs_page_recycle);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        songsRV_adapter= new SongsRVAdapter(songsRV_dataList);
        recyclerView.setAdapter(songsRV_adapter);
    }

    private void init() {
        //测试

        songsRV_dataList =  AllMediaBean.getInstance().getSongs();
    }

    /**
     * 传递给服务的，让服务处理对应点击的Item的内容我没写，你在下面对应的case添加就好
     * @author BA on 2018/2/10 0010
     * @param
     * @return
     * @exception
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position=songsRV_adapter.getLongPassPosition();
        switch (item.getItemId()){
            case SongsRVAdapter.NEXT_PLAY_ID:
                Toast.makeText(getContext(), "下一首播放", Toast.LENGTH_SHORT).show();
                return true;
            case SongsRVAdapter.ADD_TO_LIST_ID:
                Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                return true;
            case SongsRVAdapter.ALWAYS_PLAY_ID:
                Toast.makeText(getContext(), "单曲循环播放", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
