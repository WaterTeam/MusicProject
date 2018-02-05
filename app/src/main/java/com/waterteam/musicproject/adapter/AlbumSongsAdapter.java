package com.waterteam.musicproject.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.waterteam.musicproject.MainActivity;
import com.waterteam.musicproject.R;
import com.waterteam.musicproject.bean.SongsBean;
import com.waterteam.musicproject.eventsforeventbus.MusicEvent;
import com.waterteam.musicproject.eventsforeventbus.PlayingBarEvent;
import com.waterteam.musicproject.util.HandleBottomBar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BA on 2018/2/1 0001.
 *
 * @Function : 歌曲适配器
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
                .inflate(R.layout.recyclerview_item_artist_songs, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        if (viewHolder.getAdapterPosition() <= 5) {
            startAnimator(viewHolder.itemView);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                MusicEvent musicEvent = new MusicEvent();
                musicEvent.setSongsBeanList(songsRV_dataList);
                musicEvent.setPosition(position);
                musicEvent.setPlayintStatus(MusicEvent.STOP);
                EventBus.getDefault().postSticky(musicEvent);
                musicEvent.setPlayintStatus(MusicEvent.PLAY);
                EventBus.getDefault().postSticky(musicEvent);

                PlayingBarEvent playingBarEvent = new PlayingBarEvent();
                playingBarEvent.setPosition(position);
                playingBarEvent.setSongsBeanList(songsRV_dataList);

                HandleBottomBar.staticPlayingBarEvent = playingBarEvent;
                HandleBottomBar.isPlaying = true;

                playingBarEvent.setPlayingStatus(PlayingBarEvent.PLAYANEW);
                HandleBottomBar.changBottomBarView(playingBarEvent);
                //EventBus.getDefault().postSticky(playingBarEvent);
            }
        });
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
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            song_RV_item_singer = (TextView) itemView.findViewById(R.id.songs_RV_item_singer);
            song_RV_item_songName = (TextView) itemView.findViewById(R.id.song_RV_item_songName);
            song_RV_item_songTime = (TextView) itemView.findViewById(R.id.songs_RV_item_songTime);
            this.itemView = itemView;
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
}
