package ca.com.androidbinnersproject.auth;

import android.app.Activity;

/**
 * Created by jonathan_campos on 18/01/2016.
 */
public abstract class Authentication
{
	protected Activity activity;
	protected OnAuthListener onAuthListener;

    public abstract void login();

    public abstract void logout();

    public abstract void revoke();

    public void setOnAuthListener(OnAuthListener listener) {
        onAuthListener = listener;
    }
}
