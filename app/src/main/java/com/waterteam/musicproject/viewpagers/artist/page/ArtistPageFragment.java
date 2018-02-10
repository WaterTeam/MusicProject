package com.waterteam.musicproject.viewpagers.artist.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.ArtistBean;
import com.waterteam.musicproject.eventsforeventbus.EventFromTouch;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

public class ArtistPageFragment extends Fragment {

    private static final String TAG = "ArtistDetailAlbumPageFragment";

    List<ArtistBean> artistRV_dataList ;
    RecyclerView recyclerView;
    private ArtistRV_Adapter albumRV_Adapter;

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
        initAlbumPV_Data();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        albumRV_Adapter = new ArtistRV_Adapter(getActivity(),artistRV_dataList);
        recyclerView.setAdapter(albumRV_Adapter);
    }

    /**
     *  实现长按菜单
     * @author BA on 2018/2/10 0010
     * @param
     * @return
     * @exception
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //这个会获取被长按的艺术家位置，你直接可以用
        int position=albumRV_Adapter.getLongPassPosition();

        switch (item.getItemId()){
            case ArtistRV_Adapter.PLAY_ALL_SONGS_ID:
                //这里你写播放该艺术家全部歌曲的实现
                EventFromTouch eventFromTouch = new EventFromTouch();
                eventFromTouch.setStatu(EventFromTouch.NOW_PLAY);
                eventFromTouch.setPosition(0);
                eventFromTouch.setSongs(artistRV_dataList.get(position).getSongs());
                EventBus.getDefault().post(eventFromTouch);
                Toast.makeText(getContext(),"播放该艺术家的全部歌曲", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
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
