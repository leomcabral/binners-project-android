
package ca.com.androidbinnersproject.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	private final ArrayList<Fragment> fragList = new ArrayList<>();
	private final ArrayList<String> titleList = new ArrayList<>();

	public ViewPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	@Override
	public Fragment getItem(int position) {
		return fragList.get(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titleList.get(position);
	}

	@Override
	public int getCount() {
		return fragList.size();
	}

	public void AddFragment(Fragment frag, String title) {
		fragList.add(frag);
		titleList.add(title);
	}
}
