package com.waterteam.musicproject.customview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.waterteam.musicproject.MainActivity;
import com.waterteam.musicproject.R;
import com.waterteam.musicproject.eventsforeventbus.EventFromBar;
import com.waterteam.musicproject.eventsforeventbus.EventFromNotification;
import com.waterteam.musicproject.eventsforeventbus.EventToBarFromService;
import com.waterteam.musicproject.service.playmusic.service.PlayService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by CNT on 2018/2/23.
 */

public class MyNotification {
    private static final String TAG = "ProgressNotification";
    public final static String INTENT_BUTTONID_TAG = "ButtonId";
    /**
     * 通知栏按钮点击事件对应的ACTION
     */
    public final static String ACTION_BUTTON = "com.notification.intent.action.ButtonClick";
    /**
     * 标识按钮状态：是否在播放(未被使用到)
     */
    public boolean isPlay = false;
    /**
     * 通知栏按钮广播
     */
    public ButtonBroadcastReceiver receiver;

    public final static int BUTTON_PALY_ID = 1;
    public final static int BUTTON_NEXT_ID = 2;
    public final static int BUTTON_LAST_ID = 3;
    private final int REQUEST_CODE = 0xb01;

    private Context context;

    private NotificationManager notificationManager;
    private RemoteViews contentView;
    private RemoteViews contentView_small;
    private Notification notification;

    public MyNotification(Context context) {
        this.context = context;
    }

    public void initAndNotify() {
        changeNotificationStatus();
        //注册广播
        receiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        context.registerReceiver(receiver, intentFilter);

        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void changeNotification(EventToBarFromService event) {
        Log.d(TAG, "eventFromBar: ");
        switch (event.getStatu()) {
            case EventToBarFromService.PAUSE:
                if (PlayService.isPlay) {
                    contentView.setImageViewResource(R.id.big_notification_play, R.drawable.ic_pause_button);
                    contentView_small.setImageViewResource(R.id.notification_playButton, R.drawable.ic_pause_button);
                } else {
                    contentView.setImageViewResource(R.id.big_notification_play, R.drawable.ic_play_button);
                    contentView_small.setImageViewResource(R.id.notification_playButton, R.drawable.ic_play_button);
                }
                notificationManager.notify(1, notification);
                break;
            case EventToBarFromService.PAUSETOPLAY:
                if (PlayService.isPlay) {
                    contentView.setImageViewResource(R.id.big_notification_play, R.drawable.ic_pause_button);
                    contentView_small.setImageViewResource(R.id.notification_playButton, R.drawable.ic_pause_button);
                } else {
                    contentView.setImageViewResource(R.id.big_notification_play, R.drawable.ic_play_button);
                    contentView_small.setImageViewResource(R.id.notification_playButton, R.drawable.ic_play_button);
                }
                notificationManager.notify(1, notification);
                break;
            case EventToBarFromService.PLAYANEW: {
                if (PlayService.isPlay) {
                    contentView.setImageViewResource(R.id.big_notification_play, R.drawable.ic_pause_button);
                    contentView_small.setImageViewResource(R.id.notification_playButton, R.drawable.ic_pause_button);
                } else {
                    contentView.setImageViewResource(R.id.big_notification_play, R.drawable.ic_play_button);
                    contentView_small.setImageViewResource(R.id.notification_playButton, R.drawable.ic_play_button);
                }
                contentView.setTextViewText(R.id.big_notification_song, PlayService.NowPlaySong.getName());
                contentView.setTextViewText(R.id.big_notification_singer, PlayService.NowPlaySong.getAuthor());
                Uri uri1 = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), PlayService.NowPlaySong.getAlbumId());
                contentView.setImageViewUri(R.id.big_notification_image, uri1);
                contentView_small.setTextViewText(R.id.notification_song, PlayService.NowPlaySong.getName());
                contentView_small.setTextViewText(R.id.notification_singer, PlayService.NowPlaySong.getAuthor());
                contentView_small.setImageViewUri(R.id.notification_image, uri1);
                notificationManager.notify(1, notification);
            }
            case EventToBarFromService.MOVEVIewPAGER:
                if (PlayService.isPlay) {
                    contentView.setImageViewResource(R.id.big_notification_play, R.drawable.ic_pause_button);
                    contentView_small.setImageViewResource(R.id.notification_playButton, R.drawable.ic_pause_button);
                } else {
                    contentView.setImageViewResource(R.id.big_notification_play, R.drawable.ic_play_button);
                    contentView_small.setImageViewResource(R.id.notification_playButton, R.drawable.ic_play_button);
                }
                contentView.setTextViewText(R.id.big_notification_song, PlayService.NowPlaySong.getName());
                contentView.setTextViewText(R.id.big_notification_singer, PlayService.NowPlaySong.getAuthor());
                Uri uri1 = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), PlayService.NowPlaySong.getAlbumId());
                contentView.setImageViewUri(R.id.big_notification_image, uri1);
                contentView_small.setTextViewText(R.id.notification_song, PlayService.NowPlaySong.getName());
                contentView_small.setTextViewText(R.id.notification_singer, PlayService.NowPlaySong.getAuthor());
                contentView_small.setImageViewUri(R.id.notification_image, uri1);
                notificationManager.notify(1, notification);
                break;

            default:
                break;
        }
    }

    private void changeNotificationStatus() {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

        // 当用户下来通知栏时候看到的就是RemoteViews中自定义的Notification布局
        contentView = new RemoteViews(context.getPackageName(), R.layout.big_notification);
        contentView_small = new RemoteViews(context.getPackageName(), R.layout.notification);
        //设置通知栏信息
        contentView.setTextViewText(R.id.big_notification_song, PlayService.NowPlaySong.getName());
        contentView.setTextViewText(R.id.big_notification_singer, PlayService.NowPlaySong.getAuthor());
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), PlayService.NowPlaySong.getAlbumId());
        contentView.setImageViewUri(R.id.big_notification_image, uri);

        contentView_small.setTextViewText(R.id.notification_song, PlayService.NowPlaySong.getName());
        contentView_small.setTextViewText(R.id.notification_singer, PlayService.NowPlaySong.getAuthor());
        contentView_small.setImageViewUri(R.id.notification_image, uri);

        //设置点击的事件
        Intent buttonIntent = new Intent(ACTION_BUTTON);
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
        PendingIntent intent_paly = PendingIntent.getBroadcast(context, BUTTON_PALY_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.big_notification_play, intent_paly);
        contentView_small.setOnClickPendingIntent(R.id.notification_playButton, intent_paly);

        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NEXT_ID);
        PendingIntent intent_next = PendingIntent.getBroadcast(context, BUTTON_NEXT_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.big_notification_next, intent_next);
        contentView_small.setOnClickPendingIntent(R.id.notification_nextButton, intent_next);

        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_LAST_ID);
        PendingIntent intent_last = PendingIntent.getBroadcast(context, BUTTON_LAST_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.big_notification_last, intent_last);
        contentView_small.setOnClickPendingIntent(R.id.notification_lastButton, intent_last);

        mBuilder.setCustomBigContentView(contentView);
        mBuilder.setContent(contentView_small);
        notification = mBuilder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        // 当用户点击通知栏的Notification时候，切换回TaskDefineActivity。
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = pi;

        notificationManager.notify(1, notification);
    }

    /**
     * （通知栏中的点击事件是通过广播来通知的，所以在需要处理点击事件的地方注册广播即可）
     * 广播监听按钮点击事件
     */
    public class ButtonBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_BUTTON)) {
                //通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                switch (buttonId) {
                    case BUTTON_PALY_ID:
                        Log.d(TAG, "点击播放/暂停按钮");
                        onPlayClick();
                        break;
                    case BUTTON_NEXT_ID:
                        onNextClick();
                        break;
                    case BUTTON_LAST_ID:
                        onLastClick();
                        break;
                    default:
                        break;
                }
            }
        }

    }

    private void onPlayClick() {
        EventFromNotification eventFromBar = new EventFromNotification();
        if (PlayService.isPlay) {
            //当前是进行中，则暂停
            contentView.setImageViewResource(R.id.big_notification_play, R.drawable.ic_pause_button);
            contentView_small.setImageViewResource(R.id.notification_playButton, R.drawable.ic_pause_button);
            eventFromBar.setStatu(EventFromBar.PAUSE);
            EventBus.getDefault().post(eventFromBar);
        } else {
            //当前暂停，则开始
            contentView.setImageViewResource(R.id.big_notification_play, R.drawable.ic_play_button);
            contentView_small.setImageViewResource(R.id.notification_playButton, R.drawable.ic_play_button);
            eventFromBar.setStatu(EventFromBar.PAUSETOPLAY);
            EventBus.getDefault().post(eventFromBar);
        }
        notificationManager.notify(1, notification);//这里修改
    }

    private void onNextClick() {
        EventFromNotification eventFromBar = new EventFromNotification();
        eventFromBar.setStatu(EventFromBar.PLAYNEXT);
        EventBus.getDefault().post(eventFromBar);
    }

    private void onLastClick() {
        EventFromNotification eventFromBar = new EventFromNotification();
        eventFromBar.setStatu(EventFromBar.PLAYLAST);
        EventBus.getDefault().post(eventFromBar);
    }

    /**
     * 关闭通知
     */
    public void cancel() {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
        }
        if (notificationManager != null) {
            notificationManager.cancel(1);
        }
        EventBus.getDefault().unregister(this);
    }
}
