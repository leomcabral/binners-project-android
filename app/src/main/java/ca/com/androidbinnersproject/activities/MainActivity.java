
package ca.com.androidbinnersproject.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import ca.com.androidbinnersproject.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LeftNavigationDrawerMenu.FragmentDrawerListener {
    private ImageButton btnSetDate;

    private Toolbar mToolbar;
    private LeftNavigationDrawerMenu mFragmentDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //btnSetDate = (ImageButton) findViewById(R.id.activity_main_btnSelectTime);
        //btnSetDate.setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final String userLogged = getUserLogged();

        mFragmentDrawer = (LeftNavigationDrawerMenu) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mFragmentDrawer.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar, userLogged);
        mFragmentDrawer.setDrawerListener(this);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.activity_main_btnSelectTime: {
                Intent intent = new Intent(MainActivity.this, PickupActivity.class);
                startActivity(intent);
            } break;
        }*/
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }

    private String getUserLogged() {
        SharedPreferences preferences = getSharedPreferences(LoginActivity.USER_AUTHENTICATED, 0);

        return preferences.getString(LoginActivity.PROFILE_NAME, "USER");
    }
}
