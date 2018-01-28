package viewpagers.artist.page;

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
import com.waterteam.musicproject.bean.ArtistBean;

/**
 * Created by CNT on 2017/12/6.
 */

public class ArtistPageFragment extends Fragment {
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_page,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.aritist_page_recycle);
        ArtistRVAdapter artistRVAdapter=new ArtistRVAdapter(getContext());
        recyclerView.setAdapter(artistRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return  view;
    }
}