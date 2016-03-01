package ca.com.androidbinnersproject.activities.pickup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ca.com.androidbinnersproject.R;

public class PickupActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

	private final String TimeFormat = "HH:mm";

	private Calendar calendar;
	private SimpleDateFormat timeFormat;

	private Button dateButton;
	private Button timeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pickup);

		calendar = Calendar.getInstance();
		timeFormat = new SimpleDateFormat(TimeFormat, Locale.CANADA);

		dateButton = (Button) findViewById(R.id.pickup_date_button);
		timeButton = (Button) findViewById(R.id.pickup_time_button);

		dateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		timeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TimePickerDialog.newInstance(
					PickupActivity.this,
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE),
					false
				).show(getFragmentManager(), "TimePicker");
			}
		});
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
	}
}
