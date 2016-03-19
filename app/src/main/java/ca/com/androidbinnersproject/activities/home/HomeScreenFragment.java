
package ca.com.androidbinnersproject.activities.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.w3c.dom.Text;

import ca.com.androidbinnersproject.R;
import ca.com.androidbinnersproject.activities.pickup.PickupActivity;
import ca.com.androidbinnersproject.activities.pickup.PickupListFragment;
import ca.com.androidbinnersproject.adapters.ViewPagerAdapter;
import ca.com.androidbinnersproject.util.Util;

public class HomeScreenFragment extends Fragment {

	private ImageView imgProfile;
    private TextView txtProfile;
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

        txtProfile= (TextView) view.findViewById(R.id.home_screen_txtProfile);
        imgProfile= (ImageView) view.findViewById(R.id.home_screen_imgProfile);
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

    @Override
    public void onStart() {
        super.onStart();

        loadProfilePicture();
        loadProfileName();
    }

    private void loadProfileName() {
        txtProfile.setText(Util.getUserLogged(this.getActivity()));
    }

    private void loadProfilePicture() {
        Glide.with(this).load(R.drawable.nopicture).asBitmap().into(new BitmapImageViewTarget(imgProfile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imgProfile.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}
