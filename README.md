# MusicProject
# 长按事件已经写好
# 用法
在ArtistPageFragment类有个方法，你在里面处理长按出来的菜单子项点击事件
~~~java

    /**
     *  实现长按菜单
     * @author BA on 2018/2/10 0010
     * @param
     * @return
     * @exception
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //这个会获取被长按的艺术家位置，你直接可以用
        int position=albumRV_Adapter.getLongPassPosition();

        switch (item.getItemId()){
            case ArtistRV_Adapter.PLAY_ALL_SONGS_ID:
                //这里你写播放该艺术家全部歌曲的实现，用EventFromTouch类来传递事件给服务
                Toast.makeText(getContext(),"播放该艺术家的全部歌曲", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
~~~

# 其他你要处理长按事件的类，处理方法和上面差不多的都是处理播放
+ SongsPageFragment
+ ArtistDetailSongsPageFragment
+ AlbumDetailsActivity

## 另外
把eventsfroecentbus包下的不是E开头的两个类删除


