package com.waterteam.musicproject.viewpagers.songs.page;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.waterteam.musicproject.MainActivity;
import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.eventsforeventbus.EventFromTouch;
import com.waterteam.musicproject.eventsforeventbus.EventToBarFromService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */


public class SongsRVAdapter extends RecyclerView.Adapter<SongsRVAdapter.ViewHolder> {

    List<SongsBean> songsRV_dataList;

    //被长按的Item的位置
    int longPassPosition;

    //长按菜单Item的ID
    static final int NEXT_PLAY_ID=1;
    static final int ADD_TO_LIST_ID=2;
    static final int ALWAYS_PLAY_ID=3;

    public SongsRVAdapter(List<SongsBean> list) {
        songsRV_dataList = list;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        SongsBean songsRV_data = songsRV_dataList.get(position);
        holder.song_RV_item_songName.setText(songsRV_data.getName());
        holder.song_RV_item_singer.setText(songsRV_data.getAuthor());
        holder.song_RV_item_songTime.setText(songsRV_data.getFormatLenght());

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现，则让这个子项的catalog的textView显示，若不是则不View.GONE
        if (position == getPositionForSection(songsRV_data.getFirstLetter())) {
            holder.song_RV_item_catalog.setVisibility(View.VISIBLE);
            holder.song_RV_item_catalog.setText(songsRV_data.getFirstLetter());

            if (position == 0) {//开头的分隔线不显示
                holder.line.setVisibility(View.GONE);
            } else {
                holder.line.setVisibility(View.VISIBLE);
            }
        } else {
            holder.song_RV_item_catalog.setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
        }

        //设置长按监听获取Item位置
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longPassPosition=holder.getAdapterPosition();
                return false;
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item_songs, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();

                EventFromTouch eventFromTouch = new EventFromTouch();
                eventFromTouch.setSong(songsRV_dataList.get(position));
                eventFromTouch.setSongs(songsRV_dataList);
                eventFromTouch.setPosition(position);
                eventFromTouch.setStatu(EventFromTouch.NOW_PLAY);
                EventBus.getDefault().post(eventFromTouch);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                int position = viewHolder.getAdapterPosition();
                EventFromTouch eventFromTouch = new EventFromTouch();
                eventFromTouch.setSong(songsRV_dataList.get(position));
                eventFromTouch.setSongs(songsRV_dataList);
                eventFromTouch.setStatu(EventFromTouch.ADD_TO_NEXT);
                EventBus.getDefault().post(eventFromTouch);
                return true;
            }
        });
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return songsRV_dataList.size();
    }

    /**
     * copy而来：获取首字母在数据列表中第一次出现位置
     *
     * @param
     * @return 是就返回子项的position，不是就返回-1
     * @throws
     * @author CNT on 2017/12/7.
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = songsRV_dataList.get(i).getFirstLetter();
            if (catalog.equals(sortStr)) {
                return i;
            }
        }
        return -1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView song_RV_item_songName;
        TextView song_RV_item_singer;
        TextView song_RV_item_songTime;
        TextView song_RV_item_catalog;
        View line;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            song_RV_item_singer = (TextView) itemView.findViewById(R.id.songs_RV_item_singer);
            song_RV_item_songName = (TextView) itemView.findViewById(R.id.song_RV_item_songName);
            song_RV_item_songTime = (TextView) itemView.findViewById(R.id.songs_RV_item_songTime);
            song_RV_item_catalog = (TextView) itemView.findViewById(R.id.song_RV_item_catalog);
            line = itemView.findViewById(R.id.song_RV_item_line);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,NEXT_PLAY_ID,0,"下一首播放");
            menu.add(0,ADD_TO_LIST_ID,0,"添加到播放列表");
            menu.add(0,ALWAYS_PLAY_ID,0,"单曲循环");
        }
    }

    /**
     *  获取长按位置被 {@link SongsPageFragment}使用
     * @author BA on 2018/2/10 0010
     * @param
     * @return
     * @exception
     */
    public int getLongPassPosition() {
        return longPassPosition;
    }
}