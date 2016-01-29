
package ca.com.androidbinnersproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ca.com.androidbinnersproject.R;
import ca.com.androidbinnersproject.auth.Authentication;
import ca.com.androidbinnersproject.auth.FacebookAuth;
import ca.com.androidbinnersproject.auth.GoogleAuth;
import ca.com.androidbinnersproject.auth.OnAuthListener;
import ca.com.androidbinnersproject.auth.Profile;
import ca.com.androidbinnersproject.auth.TwitterAuth;
import ca.com.androidbinnersproject.auth.keys.KeyManager;
import ca.com.androidbinnersproject.util.Logger;

public class Login extends Activity implements OnAuthListener
{
    public static String IS_AUTHENTICATED = "IS_AUTHENTICATED";
    public static String USER_AUTHENTICATED = "USER_AUTHENTICATED";
    public static String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final int FROM_LOGIN = 25678;

	private KeyManager keyManager;

	private Authentication authentication;

    private Button btnGoogle;
    private Button btnFacebook;
    private Button btnTwitter;

    //private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnGoogle   = (Button) findViewById(R.id.btnGoogle);
        btnFacebook = (Button) findViewById(R.id.btnFacebook);
		btnTwitter = (Button) findViewById(R.id.btnTwitter);

        initButtonListeners();

        keyManager = new KeyManager(getResources());

        if(!keyManager.RetrieveKeys())
        	Logger.Error("Failed to retrieve keys");
    }

    private void initButtonListeners() {
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mProgressDialog = ProgressDialog.show(Login.this, "Login", "Executing Login!");

				authentication = new GoogleAuth(Login.this, Login.this, keyManager);
				authentication.login();
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				authentication = new FacebookAuth(Login.this, Login.this, keyManager);
				authentication.login();
            }
        });

        btnTwitter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				authentication = new TwitterAuth(Login.this, Login.this, keyManager);
				authentication.login();
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

        //editor.commit();
		editor.apply();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == GoogleAuth.GOOGLE_SIGN_IN)
        {
            if(resultCode == RESULT_OK)
				authentication.login();
            else
                onLoginCancel();
		}
		else if(authentication instanceof FacebookAuth)
		{
            if(resultCode == RESULT_OK)
				((FacebookAuth) authentication).getFacebookCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
        else if(authentication instanceof TwitterAuth)
		{
			((TwitterAuth) authentication).authClient.onActivityResult(requestCode, resultCode, data);
		}
    }
}
