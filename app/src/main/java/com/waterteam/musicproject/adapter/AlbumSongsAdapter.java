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
 * Created by BA on 2018/2/1 0001.
 *
 * @Function :
 */

public class AlbumSongsAdapter extends RecyclerView.Adapter<AlbumSongsAdapter.ViewHolder> {
    List<SongsBean> songsRV_dataList;

    public AlbumSongsAdapter(List<SongsBean> list) {
        songsRV_dataList = list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SongsBean songsRV_data = songsRV_dataList.get(position);
        holder.song_RV_item_songName.setText(songsRV_data.getName());
        holder.song_RV_item_singer.setText(songsRV_data.getAuthor());
        holder.song_RV_item_songTime.setText(songsRV_data.getFormatLenght());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_album_songs, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return songsRV_dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView song_RV_item_songName;
        TextView song_RV_item_singer;
        TextView song_RV_item_songTime;

        public ViewHolder(View itemView) {
            super(itemView);
            song_RV_item_singer = (TextView) itemView.findViewById(R.id.songs_RV_item_singer);
            song_RV_item_songName = (TextView) itemView.findViewById(R.id.song_RV_item_songName);
            song_RV_item_songTime = (TextView) itemView.findViewById(R.id.songs_RV_item_songTime);
        }
    }
}
