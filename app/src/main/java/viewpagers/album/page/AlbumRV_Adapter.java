package viewpagers.album.page;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.AlbumBean;
import com.waterteam.musicproject.util.GetSongsCoverUtil;

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
        ViewHolder viewHolder = new ViewHolder(view);


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

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlbumBean albumData = albumPV_dataList.get(position);
        holder.album_RV_item_title.setText(albumData.getName());
        GetSongsCoverUtil.setCover(context,albumData,holder.album_RV_item_imageView);
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
