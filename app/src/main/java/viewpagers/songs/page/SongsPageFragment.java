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
import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.util.GetSongsCoverUtil;
import com.waterteam.musicproject.util.getdatautil.GetSongUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by CNT on 2017/12/6.
 */

public class SongsPageFragment extends Fragment {
    List<SongsBean> songsRV_dataList = new ArrayList<>();
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
        SongsRVAdapter songsRV_adapter = new SongsRVAdapter(songsRV_dataList);
        recyclerView.setAdapter(songsRV_adapter);
    }

    private void init() {
        //测试

        songsRV_dataList =  AllMediaBean.getInstance().getSongs();
    }
}
