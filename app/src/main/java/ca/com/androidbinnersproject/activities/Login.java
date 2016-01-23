package ca.com.androidbinnersproject.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ca.com.androidbinnersproject.R;
import ca.com.androidbinnersproject.clazz.Authentication;
import ca.com.androidbinnersproject.clazz.FacebookAuth;
import ca.com.androidbinnersproject.clazz.GoogleAuth;
import ca.com.androidbinnersproject.clazz.OnAuthListener;
import ca.com.androidbinnersproject.clazz.Profile;

/**
 * Created by jonathan_campos on 17/01/2016.
 */
public class Login extends Activity implements OnAuthListener {
    public static String IS_AUTHENTICATED = "IS_AUTHENTICATED";
    public static String USER_AUTHENTICATED = "USER_AUTHENTICATED";
    public static String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final int FROM_LOGIN = 25678;

    private Authentication mAuthentication;

    FacebookAuth mFacebookAuth;

    private Button btnGoogle;
    private Button btnFacebook;

    //private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnGoogle   = (Button) findViewById(R.id.btnGoogle);
        btnFacebook = (Button) findViewById(R.id.btnFacebook);

        initButtonListeners();
    }

    private void initButtonListeners() {
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mProgressDialog = ProgressDialog.show(Login.this, "Login", "Executing Login!");

                mAuthentication = new GoogleAuth(Login.this, Login.this);

                mAuthentication.login();
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFacebookAuth = new FacebookAuth(Login.this, Login.this);

                mFacebookAuth.login();
            }
        });
    }

    @Override
    public void onLoginSuccess(Profile profile) {
  //      if(mProgressDialog != null)
    //        mProgressDialog.dismiss();

        Toast.makeText(this, "Logged as " + profile.getName() + " | " + profile.getEmail() + " /   " + profile.getAccessToken() , Toast.LENGTH_LONG).show();

        saveAuthenticatedUser(profile);

        setResult(RESULT_OK);

        finish();
    }

    private void saveAuthenticatedUser(Profile profile) {
        SharedPreferences preferences = getSharedPreferences(USER_AUTHENTICATED, 0);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(IS_AUTHENTICATED, true);
        editor.putString(ACCESS_TOKEN, profile.getAccessToken());
        editor.commit();
    }

    @Override
    public void onLoginError(String message) {

    }

    @Override
    public void onLoginCancel() {

    }

    @Override
    public void onRevoke() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == GoogleAuth.GOOGLE_SIGN_IN) {
            if(resultCode == RESULT_OK) {
                mAuthentication.login();
            } else {
                onLoginCancel();
            }
        }

        if(mFacebookAuth != null) {
            if(resultCode == RESULT_OK) {
                mFacebookAuth.getFacebookCallbackManager().onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
