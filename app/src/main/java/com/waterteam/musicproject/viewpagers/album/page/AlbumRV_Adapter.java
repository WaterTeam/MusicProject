package com.waterteam.musicproject.viewpagers.album.page;

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
import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.AlbumBean;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.util.GetCoverUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

public class AlbumRV_Adapter extends RecyclerView.Adapter<AlbumRV_Adapter.ViewHolder> {
    private static final String TAG = "AlbumRV_Adapter";
    private Context context;
    List<AlbumBean> albumPV_dataList;

    public AlbumRV_Adapter(Context context, List<AlbumBean> list) {
        albumPV_dataList = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item_album, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);


        /**
         * 这个方法用于处理子项的点击事件，需要覆写
         * @author CNT on 2017/12/6.
         * @param
         * @return
         * @exception
         */
        viewHolder.album_RV_item_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AlbumDetailsActivity.class);
                intent.putExtra("position",viewHolder.getAdapterPosition());
                context.startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation
                                ((AppCompatActivity)(context),viewHolder.album_RV_item_imageView,"albumImage").toBundle());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlbumBean albumData = albumPV_dataList.get(position);
        holder.album_RV_item_title.setText(albumData.getName());
        GetCoverUtil.setCover(context, albumData, holder.album_RV_item_imageView);

        holder.album_RV_item_title.setText(albumData.getName());

        List<SongsBean> songsBeanList = albumData.getSongs();

        holder.album_RV_item_song1_name.setText(songsBeanList.get(0).getName());
        holder.album_RV_item_song1_time.setText(songsBeanList.get(0).getFormatLenght());
        if (songsBeanList.size() >= 2) {
            holder.album_RV_item_song2_name.setText(songsBeanList.get(1).getName());
            holder.album_RV_item_song2_time.setText(songsBeanList.get(1).getFormatLenght());
        } else {//为了防止recycyleview产生的控件复用引起的控件内数据混乱，需要每次都判断
            holder.album_RV_item_song2_name.setText("");
            holder.album_RV_item_song2_time.setText("");
        }
        if (songsBeanList.size() >= 3) {
            holder.album_RV_item_song3_name.setText(songsBeanList.get(2).getName());
            holder.album_RV_item_song3_time.setText(songsBeanList.get(2).getFormatLenght());
        } else {
            holder.album_RV_item_song3_name.setText("");
            holder.album_RV_item_song3_time.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return albumPV_dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView album_RV_item_title;
        ImageView album_RV_item_imageView;
        View album_RV_item_View;

        TextView album_RV_item_song1_name;
        TextView album_RV_item_song2_name;
        TextView album_RV_item_song3_name;

        TextView album_RV_item_song1_time;
        TextView album_RV_item_song2_time;
        TextView album_RV_item_song3_time;

        public ViewHolder(View view) {
            super(view);
            album_RV_item_imageView = (ImageView) view.findViewById(R.id.album_RV_item_imageView);
            album_RV_item_title = (TextView) view.findViewById(R.id.album_RV_item_title);
            album_RV_item_song1_name = (TextView) view.findViewById(R.id.album_RV_item_song1_name);
            album_RV_item_song1_time = (TextView) view.findViewById(R.id.album_RV_item_song1_time);
            album_RV_item_song2_name = (TextView) view.findViewById(R.id.album_RV_item_song2_name);
            album_RV_item_song2_time = (TextView) view.findViewById(R.id.album_RV_item_song2_time);
            album_RV_item_song3_name = (TextView) view.findViewById(R.id.album_RV_item_song3_name);
            album_RV_item_song3_time = (TextView) view.findViewById(R.id.album_RV_item_song3_time);
            album_RV_item_View = view;
        }
    }
}
