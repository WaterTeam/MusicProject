package viewpagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

public class MyPageAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;
    /**
     * 构造函数初始化适配器时，要传入数据源；
     * @author CNT on 2017/12/5.
     * @param
     * @return
     * @exception
     */
    public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        fragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * 复写这个方法去掉super，禁止销毁碎片，这样滑动不会卡顿
     * @author BA on 2018/1/26 0026
     * @param
     * @return
     * @exception
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
