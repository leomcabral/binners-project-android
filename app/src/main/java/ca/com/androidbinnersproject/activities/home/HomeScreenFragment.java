
package ca.com.androidbinnersproject.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.com.androidbinnersproject.R;
import ca.com.androidbinnersproject.activities.pickup.PickupActivity;
import ca.com.androidbinnersproject.activities.pickup.PickupListFragment;
import ca.com.androidbinnersproject.adapters.ViewPagerAdapter;

public class HomeScreenFragment extends Fragment {

	private TabLayout tabLayout;
	private ViewPager viewPager;

	private Button newPickupButton;

	public HomeScreenFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

		ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
		adapter.AddFragment(new PickupListFragment(), "Upcoming Pick-Ups");
		adapter.AddFragment(new PickupListFragment(), "Completed Pick-Ups");

		viewPager = (ViewPager) view.findViewById(R.id.home_viewpager);
		viewPager.setAdapter(adapter);

		tabLayout = (TabLayout) view.findViewById(R.id.home_tablayout);
		tabLayout.setupWithViewPager(viewPager);

		newPickupButton = (Button) view.findViewById(R.id.home_newpickup_button);
		newPickupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), PickupActivity.class);
				startActivity(intent);
			}
		});

		return view;
	}
}
