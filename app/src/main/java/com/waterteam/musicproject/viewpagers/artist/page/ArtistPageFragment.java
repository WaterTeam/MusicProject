package com.waterteam.musicproject.viewpagers.artist.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.ArtistBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

public class ArtistPageFragment extends Fragment {

    private static final String TAG = "ArtistDetailAlbumPageFragment";

    List<ArtistBean> artistRV_dataList ;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_page,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.album_page_recycle);
        return  view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        initAlbumPV_Data();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ArtistRV_Adapter albumRV_Adaper = new ArtistRV_Adapter(getActivity(),artistRV_dataList);
        recyclerView.setAdapter(albumRV_Adaper);
    }

    /**
     * initAlbumPV_Data()这个方法用于初始化RecycleView的数据,需要覆写
     * @author CNT on 2017/12/6.
     * @param
     * @return
     * @exception
     */
    private void initAlbumPV_Data(){

        artistRV_dataList= AllMediaBean.getInstance().getArtists();
    }
}
