
package ca.com.androidbinnersproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import ca.com.androidbinnersproject.R;
import ca.com.androidbinnersproject.auth.AppAuth;
import ca.com.androidbinnersproject.auth.Authentication;
import ca.com.androidbinnersproject.auth.FacebookAuth;
import ca.com.androidbinnersproject.auth.GoogleAuth;
import ca.com.androidbinnersproject.auth.OnAuthListener;
import ca.com.androidbinnersproject.models.Profile;
import ca.com.androidbinnersproject.auth.TwitterAuth;
import ca.com.androidbinnersproject.auth.keys.KeyManager;
import ca.com.androidbinnersproject.util.Logger;
import ca.com.androidbinnersproject.util.Util;


public class LoginActivity extends AppCompatActivity implements OnAuthListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    public static String IS_AUTHENTICATED = "IS_AUTHENTICATED";
    public static String USER_AUTHENTICATED = "USER_AUTHENTICATED";
    public static String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final int FROM_LOGIN = 25678;

	private KeyManager keyManager;

	private Authentication authentication;

    private ViewSwitcher vsBinnerResident;

    private Button btnGoogle;
    private Button btnFacebook;
    private Button btnTwitter;
    private Button btnLogin;
    private Button btnCreateAccount;

    private EditText edtEmail;
    private EditText edtPassword;

    private ToggleButton binnerResidentSelector;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        vsBinnerResident = (ViewSwitcher) findViewById(R.id.login_vs_binner_resident);

        btnGoogle   = (Button) findViewById(R.id.login_button_google);
        btnFacebook = (Button) findViewById(R.id.login_button_fb);
		btnTwitter  = (Button) findViewById(R.id.login_button_twitter);
        btnLogin    = (Button) findViewById(R.id.login_login_button);
        btnCreateAccount = (Button) findViewById(R.id.login_btnCreateAccount);

        edtEmail    = (EditText) findViewById(R.id.login_email_field);
        edtPassword = (EditText) findViewById(R.id.login_password_field);

        binnerResidentSelector = (ToggleButton) findViewById(R.id.login_binner_resident_selector);

        keyManager = new KeyManager(getResources());

        if(!keyManager.RetrieveKeys())
        	Logger.Error("Failed to retrieve keys");

        initListeners();

        showAboutAppUI();
    }

    /**
     * The intent AboutAppActivity will be shown before the login UI
     */
    private void showAboutAppUI() {
        Intent intent = new Intent(this, AboutAppActivity.class);
        startActivity(intent);
    }

    private void initListeners() {

        binnerResidentSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked) {
                    vsBinnerResident.showPrevious();
                } else {
                    vsBinnerResident.showNext();
                }
            }
        });

        btnGoogle.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnTwitter.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createUserIntent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivityForResult(createUserIntent, CreateAccountActivity.CREATE_ACCOUNT_RESULT);
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
    }

    @Override
    public void onLoginError(String message) {
        dismissPDialog();
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GoogleAuth.GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            ((GoogleAuth) authentication).handleSignInResult(result);
		}
		else if(authentication instanceof FacebookAuth) {
            if(resultCode == RESULT_OK)
				((FacebookAuth) authentication).getFacebookCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
        else if(authentication instanceof TwitterAuth) {
			((TwitterAuth) authentication).authClient.onActivityResult(requestCode, resultCode, data);
		}

        if(requestCode == CreateAccountActivity.CREATE_ACCOUNT_RESULT) {
            if(resultCode == RESULT_OK) {

            }
        }
    }

    public boolean isEditFilled() {
        return edtEmail.getText().toString().length() > 0 && edtPassword.getText().toString().length() > 0;
    }

    @Override
    public void onClick(View view) {
        if(!Util.hasInternetConnection(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, R.string.has_no_connection, Toast.LENGTH_SHORT).show();

            return;
        }

        mProgressDialog = ProgressDialog.show(LoginActivity.this, "Login", this.getString(R.string.executing_sign_in));

        switch (view.getId()) {

            case R.id.login_button_fb:
                if(authentication == null && !(authentication instanceof FacebookAuth) )
                    authentication = new FacebookAuth(LoginActivity.this, LoginActivity.this, keyManager);
                authentication.login();
            break;

            case R.id.login_button_twitter:
                if(authentication == null && !(authentication instanceof TwitterAuth) )
                    authentication = new TwitterAuth(LoginActivity.this, LoginActivity.this, keyManager);
                authentication.login();
            break;

            case R.id.login_button_google:
                if(authentication == null && !(authentication instanceof GoogleAuth) )
                    authentication = new GoogleAuth(LoginActivity.this, LoginActivity.this, keyManager);
                authentication.login();
            break;

            case R.id.login_login_button:
                if (isEditFilled()) {
                    if(Util.isEmailValid(edtEmail.getText().toString())) {
                        authentication = new AppAuth(LoginActivity.this, edtEmail.getText().toString(),
                                					edtPassword.getText().toString(), LoginActivity.this);

                        authentication.login();
                    } else {
                        Toast.makeText(LoginActivity.this, getApplicationContext().getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                        dismissPDialog();
                    }
                } else {
                    dismissPDialog();
                    Toast.makeText(LoginActivity.this, getApplicationContext().getString(R.string.fill_login), Toast.LENGTH_SHORT).show();
                }
            break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
