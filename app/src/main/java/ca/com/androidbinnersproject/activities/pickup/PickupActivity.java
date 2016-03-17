package ca.com.androidbinnersproject.activities.pickup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import ca.com.androidbinnersproject.R;

public class PickupActivity extends AppCompatActivity {

	public enum PickupStage {
		Date,
		Time,
		Location,
		Confirm
	}

	private PickupStage currentStage;

	private Button nextButton;
	private Button backButton;
	private FrameLayout container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pickup);

		setFragmentStage(PickupStage.Date);

		nextButton = (Button) findViewById(R.id.pickup_next_button);
		backButton = (Button) findViewById(R.id.pickup_back_button);
		container = (FrameLayout) findViewById(R.id.pickup_container);

		//TODO find elegant way to switch stages (convert enum to int?)
		View.OnClickListener buttonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.pickup_next_button) {

					setFragmentStage(PickupStage.Time);

				} else {

					setFragmentStage(PickupStage.Date);

				}
			}
		};

		nextButton.setOnClickListener(buttonListener);
		backButton.setOnClickListener(buttonListener);
	}

	private void setFragmentStage(PickupStage stage) {

		if(currentStage == stage)
			return;

		currentStage = stage;

		if(container != null && container.getChildCount() > 0)
			container.removeAllViews();

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		switch(currentStage) {
			case Date:
				transaction.add(R.id.pickup_container, new SelectDateFragment());
			break;

			case Time:
				transaction.add(R.id.pickup_container, new TimePickerFragment());
			break;

			case Location:
			break;

			case Confirm:
			break;
		}

		transaction.commit();
	}
}
