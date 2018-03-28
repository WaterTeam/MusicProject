package com.waterteam.musicproject.customview.bottom.bar.playing;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.util.GetCoverUtil;

import java.util.List;

/**
 * Created by BA on 2018/3/28 0028.
 *
 * @Function :
 */

public class PlayingAdapter extends RecyclerView.Adapter <PlayingAdapter.ViewHolder>{
    List<SongsBean> list;
    Context context;

    public PlayingAdapter(Context context, List<SongsBean> list){
        this.list=list;
        this.context=context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.play_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_playing_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetCoverUtil.setCover(context,list.get(position),holder.imageView,400);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
