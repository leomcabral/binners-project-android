package ca.com.androidbinnersproject.activities;

import android.app.Activity;
import android.os.Bundle;

import ca.com.androidbinnersproject.R;

/**
 * Created by jonathan_campos on 18/02/2016.
 */
public class CreateAccountActivity extends Activity{
    public static final int CREATE_ACCOUNT_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);
    }
}
