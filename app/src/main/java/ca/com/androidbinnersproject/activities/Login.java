package ca.com.androidbinnersproject.activities;

import android.app.Activity;
import android.os.Bundle;

import ca.com.androidbinnersproject.R;

/**
 * Created by jonathan_campos on 17/01/2016.
 */
public class Login extends Activity {

    public static String USER_AUTHENTICATED = "USER_AUTHENTICATED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
}
