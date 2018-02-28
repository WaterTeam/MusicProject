package com.waterteam.musicproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.SongsBean;

import java.util.List;

/**
 * Created by CNT on 2018/2/26.
 */

public class SecondBottomAdapter extends RecyclerView.Adapter<SecondBottomAdapter.ViewHolder> {
    private List<SongsBean> songsRV_dataList;

    public SecondBottomAdapter(List<SongsBean> songsRV_dataList){
        this.songsRV_dataList = songsRV_dataList;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SongsBean songsRV_data = songsRV_dataList.get(position);
        holder.song_RV_item_songName.setText(songsRV_data.getName());
        holder.song_RV_item_singer.setText("- "+songsRV_data.getAuthor());
        holder.song_RV_item_number.setText(position+1+"");
    }


    @Override
    public int getItemCount() {
        return songsRV_dataList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_item_bottombar, parent, false);
        SecondBottomAdapter.ViewHolder viewHolder = new SecondBottomAdapter.ViewHolder(view);
        return viewHolder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView song_RV_item_songName;
        TextView song_RV_item_singer;
        TextView song_RV_item_number;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            song_RV_item_singer = (TextView) itemView.findViewById(R.id.second_bottomBar_singer);
            song_RV_item_songName = (TextView) itemView.findViewById(R.id.second_bottomBar_songName);
            song_RV_item_number = (TextView) itemView.findViewById(R.id.second_bottomBar_number);
            this.itemView = itemView;
        }
    }
}
