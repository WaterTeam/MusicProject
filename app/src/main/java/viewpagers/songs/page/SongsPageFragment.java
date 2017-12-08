package viewpagers.songs.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waterteam.musicproject.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import viewpagers.album.page.AlbumPV_Data;

/**
 * Created by CNT on 2017/12/6.
 */

public class SongsPageFragment extends Fragment {
    List<SongsRV_Data> songsRV_dataList = new ArrayList<>();
    RecyclerView recyclerView;

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
        SongsRV_Adapter songsRV_adapter = new SongsRV_Adapter(songsRV_dataList);
        recyclerView.setAdapter(songsRV_adapter);
    }

    private void init() {
        //测试
        SongsRV_Data songsRV_data = new SongsRV_Data();
        songsRV_data.setSinger("陈易迅");
        songsRV_data.setSongName("爱如潮水");
        songsRV_data.setSongTime("03.25");
        songsRV_dataList.add(songsRV_data);

        SongsRV_Data songsRV_data2 = new SongsRV_Data();
        songsRV_data2.setSinger("陈易迅");
        songsRV_data2.setSongName("你把我灌醉");
        songsRV_data2.setSongTime("03.25");
        songsRV_dataList.add(songsRV_data2);

        SongsRV_Data songsRV_data1 = new SongsRV_Data();
        songsRV_data1.setSinger("陈易迅");
        songsRV_data1.setSongName("哈哈");
        songsRV_data1.setSongTime("03.25");
        songsRV_dataList.add(songsRV_data1);

        SongsRV_Data songsRV_data3 = new SongsRV_Data();
        songsRV_data3.setSinger("陈易迅");
        songsRV_data3.setSongName("你在干什么");
        songsRV_data3.setSongTime("03.25");
        songsRV_dataList.add(songsRV_data3);

        Collections.sort(songsRV_dataList);

    }
}
