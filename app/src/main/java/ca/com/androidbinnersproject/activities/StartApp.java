package ca.com.androidbinnersproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * Created by jonathan_campos on 17/01/2016.
 */
public class StartApp extends Activity {
    private boolean logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent();

        if(isLogged()) {
            intent.setClass(this, MainActivity.class);
        } else {
            intent.setClass(this, Login.class);
        }

        startActivity(intent);
    }

    public boolean isLogged() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean(Login.USER_AUTHENTICATED, false);
    }
}
