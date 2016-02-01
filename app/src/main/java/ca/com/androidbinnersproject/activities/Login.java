
package ca.com.androidbinnersproject.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.com.androidbinnersproject.R;
import ca.com.androidbinnersproject.apis.AppLoginService;
import ca.com.androidbinnersproject.apis.BaseAPI;
import ca.com.androidbinnersproject.auth.AppAuth;
import ca.com.androidbinnersproject.auth.Authentication;
import ca.com.androidbinnersproject.auth.FacebookAuth;
import ca.com.androidbinnersproject.auth.GoogleAuth;
import ca.com.androidbinnersproject.auth.OnAuthListener;
import ca.com.androidbinnersproject.auth.Profile;
import ca.com.androidbinnersproject.auth.TwitterAuth;
<<<<<<< HEAD
import ca.com.androidbinnersproject.auth.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
=======
import ca.com.androidbinnersproject.auth.keys.KeyManager;
import ca.com.androidbinnersproject.util.Logger;
>>>>>>> upstream/dev

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
    private Button btnLogin;

    private EditText edtEmail;
    private EditText edtPassword;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnGoogle   = (Button) findViewById(R.id.btnGoogle);
        btnFacebook = (Button) findViewById(R.id.btnFacebook);
		btnTwitter  = (Button) findViewById(R.id.btnTwitter);
        btnLogin    = (Button) findViewById(R.id.btnLogin);
        
        edtEmail    = (EditText) findViewById(R.id.edtUser);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        initButtonListeners();

        keyManager = new KeyManager(getResources());

        if(!keyManager.RetrieveKeys())
        	Logger.Error("Failed to retrieve keys");
    }

    private void initButtonListeners() {
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = ProgressDialog.show(Login.this, "Login", "Executing Google SignIn!");

				authentication = new GoogleAuth(Login.this, Login.this, keyManager);
				authentication.login();
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                mProgressDialog = ProgressDialog.show(Login.this, "Login", "Executing Facebook SignIn!");
				authentication = new FacebookAuth(Login.this, Login.this);
=======
				authentication = new FacebookAuth(Login.this, Login.this, keyManager);
>>>>>>> upstream/dev
				authentication.login();
            }
        });

        btnTwitter.setOnClickListener(new View.OnClickListener() {
			@Override
<<<<<<< HEAD
			public void onClick(View v)
			{
                mProgressDialog = ProgressDialog.show(Login.this, "Login", "Executing Twitter SignIn!");
				authentication = new TwitterAuth(Login.this, Login.this);
=======
			public void onClick(View v) {
				authentication = new TwitterAuth(Login.this, Login.this, keyManager);
>>>>>>> upstream/dev
				authentication.login();
			}
		});

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditFilled()) {
                    mProgressDialog = ProgressDialog.show(Login.this, "Login", "Executing App SignIn!");

                    authentication = new AppAuth(edtEmail.getText().toString(),
                            edtPassword.getText().toString(), Login.this);

                    authentication.login();
                } else {
                    Toast.makeText(Login.this, getApplicationContext().getString(R.string.fill_login), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onLoginSuccess(Profile profile) {
        dismissPDialog();

        Toast.makeText(this, "Logged as " + profile.getName() + " | " + profile.getEmail() + " /   " + profile.getToken() , Toast.LENGTH_LONG).show();

        saveAuthenticatedUser(profile.getToken());

        setResult(RESULT_OK);

        finish();

<<<<<<< HEAD
=======
        editor.putBoolean(IS_AUTHENTICATED, true);
        editor.putString(ACCESS_TOKEN, profile.getAccessToken());

        //editor.commit();
		editor.apply();
>>>>>>> upstream/dev
    }

    @Override
    public void onLoginError(String message) {
        dismissPDialog();
    }

    @Override
    public void onLoginCancel() {
        dismissPDialog();
    }

    @Override
    public void onRevoke() {
        dismissPDialog();
    }

    private void dismissPDialog() {
        if(mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    private void saveAuthenticatedUser(String token) {
        SharedPreferences preferences = getSharedPreferences(USER_AUTHENTICATED, 0);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(IS_AUTHENTICATED, true);
        editor.putString(ACCESS_TOKEN, token);
        editor.commit();
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

    public boolean isEditFilled() {
        return edtEmail.getText().toString().length() > 0 && edtPassword.getText().toString().length() > 0;
    }
}
