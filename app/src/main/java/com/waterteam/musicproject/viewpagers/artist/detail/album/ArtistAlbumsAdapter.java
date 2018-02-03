package com.waterteam.musicproject.viewpagers.artist.detail.album;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.waterteam.musicproject.AlbumDetailsActivity;
import com.waterteam.musicproject.ArtistDetailsActivity;
import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.AlbumBean;
import com.waterteam.musicproject.util.GetCoverUtil;

import java.util.List;

/**
 * Created by BA on 2018/2/3 0003.
 *
 * @Function : 艺术家详情页里面的专辑列表的适配器 用在 {@link ArtistDetailAlbumPageFragment}
 */

public class ArtistAlbumsAdapter extends RecyclerView.Adapter <ArtistAlbumsAdapter.ViewHolder>{
    private List<AlbumBean> albums;
    private Context context;
    private int artistPosition;

    public ArtistAlbumsAdapter(Context context,List<AlbumBean> albums,int artistPosition) {
        this.albums=albums;
        this.context=context;
        this.artistPosition=artistPosition;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView albumName;
        TextView songsCount;
        ImageView albumCover;

        public ViewHolder(View itemView) {
            super(itemView);
            albumName=(TextView)itemView.findViewById(R.id.album_name);
            albumCover=(ImageView)itemView.findViewById(R.id.album_image);
            songsCount=(TextView)itemView.findViewById(R.id.songs_count);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_carview_album,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        //设置监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AlbumDetailsActivity.class);
                intent.putExtra("albumPosition",holder.getAdapterPosition());
                intent.putExtra("artistPosition",artistPosition);
                context.startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation
                                ((AppCompatActivity)(context),holder.albumCover,"albumCover").toBundle());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlbumBean albumBean= albums.get(position);
        GetCoverUtil.setCover(context,albumBean, holder.albumCover);
        holder.albumName.setText(albumBean.getName());
        holder.songsCount.setText(albumBean.getSongsCount()+"首歌曲");
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }
}
