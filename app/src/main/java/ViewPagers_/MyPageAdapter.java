package ViewPagers_;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
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
}
