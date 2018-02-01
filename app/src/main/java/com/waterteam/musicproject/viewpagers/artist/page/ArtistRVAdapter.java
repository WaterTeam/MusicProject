
package com.waterteam.musicproject.viewpagers.artist.page;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.AllMediaBean;
import com.waterteam.musicproject.bean.ArtistBean;
import com.waterteam.musicproject.util.GetCoverUtil;

import java.util.List;

/**
 * Created by BA on 2018/1/25 0025.
 *
 * @Function : 艺术家界面的适配器
 */

public class ArtistRVAdapter extends RecyclerView.Adapter<ArtistRVAdapter.ViewHolder> {
    private List<ArtistBean> artists= AllMediaBean.getInstance().getArtists();
    private Context context;
    private static final String TAG = "ArtistRVAdapter";

     ArtistRVAdapter(Context context){
        this.context=context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView headImage;    //歌手头像
        TextView artistName;//歌手名字
        TextView songsCount; //歌手歌曲数量
        RecyclerView recyclerView; //歌曲

         ViewHolder(View itemView) {
            super(itemView);
            headImage=(ImageView)itemView.findViewById(R.id.head_imageviwe);
            artistName=(TextView)itemView.findViewById(R.id.artist_name);
            songsCount=(TextView)itemView.findViewById(R.id.songs_count);
            Log.d(TAG, "ViewHolder: "+ artistName);
            recyclerView=(RecyclerView)itemView.findViewById(R.id.songs_rv);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_artis, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArtistBean artist =artists.get(position);
        GetCoverUtil.setCover(context,artist.getSongs().get(artist.getSongsCount()-1)
                ,holder.headImage);
        holder.artistName.setText(artist.getName());
        holder.songsCount.setText(artist.getSongsCount()+"首歌曲");

        holder.recyclerView.setAdapter(new SongsCarViewRVAdapter(context,artist.getSongs()));
        ScrollLinearLayoutManager linearLayoutManager= new ScrollLinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(linearLayoutManager);

        //让前两个横向的RecyclerView自动滚动一段距离
        if (position==0||position==1){
            holder.recyclerView.smoothScrollToPosition(artist.getSongs().size()/2);
        }
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }
}

