package ca.com.androidbinnersproject.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by jonathan_campos on 08/02/2016.
 *
 * This class is going to be used to show the some views that will explain
 * what's the propose of this app.
 */
public class StartAppPageAdapter extends FragmentPagerAdapter{

    private List<Fragment> mFragments;

    public StartAppPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
