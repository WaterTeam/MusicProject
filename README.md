# MusicProject
# Bar操作逻辑
1.点击bar上的播放按钮
2.处理bar的点击事件的类:HandleBottomBarTouchUtil,会发送EventFromBar事件给服务
3.服务获取事件，判断类型，做相应处理
4.服务处理完后，发送事件通知Bar界面做出改变（这里我没写，不急着写）

# 点击歌曲和长按歌曲的操作逻辑（你之前写的监听事件我已经全部注释）
1.点击歌曲，会发送事件到服务，
2.服务做相应处理（具体在下面）
3.服务处理后，如果是马上播放就直接发送事件通知Bar界面做出改变 （这里我没写，不急着写）

# 长按歌曲事件
会有两种选择
+ 下一首播放，
1.发送相应的事件给服务
2.服务处理（我已经处理好）

+ 添加到播放列表，
1.发送相应的事件给服务
2.服务处理（我已经处理好）

# 你要看的类
+ EventFromBar
+ EventFromTouch
+ PlayService
+ HandleBottomBarTouchUtil
+ waitingPlaySongs

# 播放列表的展示还没写，你可以直接打印

# 事件类型
事件我暂时分为两种，放在了：package com.waterteam.musicproject.eventsforeventbus;
+ 第一种是bar传给服务的事件类型，类名：EventFromBar
+ 第二种是触摸事件的事件：类名:eventFromTouch

第二种其实是歌曲点击，还有长按时的触摸事件，还包括了点击全部播放的事件，你可以看看具体类

# 服务与播放列表
按照约定，Bar不会接触播放列表，播放列表由服务控制，Bar只需要传控制歌曲播放的信号。
其实服务只是取控制一个值，也就是position，这个代表当前正在播放的音乐处于播放列表的哪一个位置

# 触摸事件与服务
一共有3种比较重要关于触摸的事件状态
+ 点击歌曲后直接播放：加入到播放列表的逻辑我已经写好，你只需要看看有没加入成功，有就从事件拿出歌曲，然后播放

+ 添加到下一首播放：同上

+ 添加到播放列表：同上

+ 播放全部歌曲；加入播放列表的逻辑我已经写好，你只需要用Position播放即可

# 我发现你处理的MediaPlayer会经常报错，你要注意一下，看看使用MediaPlayer的方法和逻辑有没有错
 E/MediaPlayer: start called in state 1, mPlayer(0x0)
 E/MediaPlayer: error (-38, 0)
 E/MediaPlayer: Error (-38,0)

