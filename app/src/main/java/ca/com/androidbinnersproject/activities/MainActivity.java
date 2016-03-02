
package ca.com.androidbinnersproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import ca.com.androidbinnersproject.R;
import ca.com.androidbinnersproject.activities.pickup.PickupActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton btnSetDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSetDate = (ImageButton) findViewById(R.id.activity_main_btnSelectTime);
        btnSetDate.setOnClickListener(this);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_btnSelectTime: {
                Intent intent = new Intent(MainActivity.this, PickupActivity.class);
                startActivity(intent);
            } break;
        }
    }
}
