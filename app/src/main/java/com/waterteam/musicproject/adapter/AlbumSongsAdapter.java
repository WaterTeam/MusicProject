package com.waterteam.musicproject.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.eventsforeventbus.EventFromTouch;
import com.waterteam.musicproject.viewpagers.songs.page.SongsPageFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BA on 2018/2/1 0001.
 *
 * @Function : 歌曲适配器
 */

public class AlbumSongsAdapter extends RecyclerView.Adapter<AlbumSongsAdapter.ViewHolder> {
    List<SongsBean> songsRV_dataList;
    //被长按的Item的位置
    int longPassPosition;

    //长按菜单Item的ID
    public static final int NEXT_PLAY_ID=4;
    public static final int ADD_TO_LIST_ID=5;
    public static final int ALWAYS_PLAY_ID=6;

    public AlbumSongsAdapter(List<SongsBean> list) {
        songsRV_dataList = list;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SongsBean songsRV_data = songsRV_dataList.get(position);
        holder.song_RV_item_songName.setText(songsRV_data.getName());
        holder.song_RV_item_singer.setText(songsRV_data.getAuthor());
        holder.song_RV_item_songTime.setText(songsRV_data.getFormatLenght());

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_item_artist_songs, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        if (viewHolder.getAdapterPosition() <= 5) {
            startAnimator(viewHolder.itemView);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                EventFromTouch eventFromTouch = new EventFromTouch();
                eventFromTouch.setSongs(songsRV_dataList);
                eventFromTouch.setSong(songsRV_dataList.get(position));
                eventFromTouch.setPosition(position);
                //eventFromTouch.setSongs(songsRV_dataList);
                eventFromTouch.setStatu(EventFromTouch.NOW_PLAY);
                EventBus.getDefault().post(eventFromTouch);
            }
        });
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return songsRV_dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView song_RV_item_songName;
        TextView song_RV_item_singer;
        TextView song_RV_item_songTime;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            song_RV_item_singer = (TextView) itemView.findViewById(R.id.songs_RV_item_singer);
            song_RV_item_songName = (TextView) itemView.findViewById(R.id.song_RV_item_songName);
            song_RV_item_songTime = (TextView) itemView.findViewById(R.id.songs_RV_item_songTime);
            this.itemView = itemView;

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,NEXT_PLAY_ID,0,"下一首播放");
            menu.add(0,ADD_TO_LIST_ID,0,"添加到播放列表");
            menu.add(0,ALWAYS_PLAY_ID,0,"单曲循环");
        }
    }

    private void startAnimator(View view) {
        view.setTranslationY(500);
        view.setAlpha(0f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0);

        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(500);
        animatorSet.setStartDelay(300);
        animatorSet.play(translationY).with(alpha);
        animatorSet.start();
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
