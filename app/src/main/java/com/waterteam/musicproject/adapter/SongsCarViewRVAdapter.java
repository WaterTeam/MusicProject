package com.waterteam.musicproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.util.GetSongsCoverUtil;

import java.util.List;

/**
 * Created by BA on 2018/1/25 0025.
 *
 * @Function : 歌曲水平方向显示的适配器，目前在 {@link viewpagers.artist.page.ArtistRVAdapter}使用
 */

public class SongsCarViewRVAdapter extends RecyclerView.Adapter<SongsCarViewRVAdapter.ViewHolder> {
    private List<SongsBean> songs;
    private Context context;

    public SongsCarViewRVAdapter(Context context, List<SongsBean> songs) {
        this.songs = songs;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView albumImage;
        TextView songName;

         ViewHolder(View view) {
            super(view);
            albumImage = (ImageView) view.findViewById(R.id.album_image);
            songName = (TextView) view.findViewById(R.id.song_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_carview_songs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SongsBean song = songs.get(position);
        GetSongsCoverUtil.setCover(context, song, holder.albumImage);
        holder.songName.setText(song.getName());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}
