package viewpagers.album.page;

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
import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

public class AlbumPageFragment extends Fragment {
    List<AlbumPV_Data> albumPV_dataList = new ArrayList<>();
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_page,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.album_page_recycle);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initAlbumPV_Data();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        AlbumRV_Adapter albumRV_Adaper = new AlbumRV_Adapter(albumPV_dataList);
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

    }
}