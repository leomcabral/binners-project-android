
package ca.com.androidbinnersproject.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ca.com.androidbinnersproject.R;
import ca.com.androidbinnersproject.auth.AppAuth;
import ca.com.androidbinnersproject.auth.Authentication;
import ca.com.androidbinnersproject.auth.FacebookAuth;
import ca.com.androidbinnersproject.auth.GoogleAuth;
import ca.com.androidbinnersproject.auth.OnAuthListener;
import ca.com.androidbinnersproject.auth.Profile;
import ca.com.androidbinnersproject.auth.TwitterAuth;
import ca.com.androidbinnersproject.auth.keys.KeyManager;
import ca.com.androidbinnersproject.util.Logger;
import ca.com.androidbinnersproject.util.Util;


public class Login extends Activity implements OnAuthListener, View.OnClickListener {
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

    private ImageView binnerResidentSelector;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnGoogle   = (Button) findViewById(R.id.login_button_google);
        btnFacebook = (Button) findViewById(R.id.login_button_fb);
		btnTwitter  = (Button) findViewById(R.id.login_button_twitter);
        btnLogin    = (Button) findViewById(R.id.login_login_button);

        edtEmail    = (EditText) findViewById(R.id.login_email_field);
        edtPassword = (EditText) findViewById(R.id.login_password_field);

        binnerResidentSelector = (ImageView) findViewById(R.id.login_binner_resident_selector);

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
        btnGoogle.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnTwitter.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

		LoginEditTextFieldFocusChangeListener focusChangeListener = new LoginEditTextFieldFocusChangeListener();

        edtEmail.setOnFocusChangeListener(focusChangeListener);
        edtPassword.setOnFocusChangeListener(focusChangeListener);

        binnerResidentSelector.setOnTouchListener(new LoginSelectorTouchListener());
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
        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
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
            if(resultCode == RESULT_OK)
				authentication.login();
            else
                onLoginCancel();
		}
		else if(authentication instanceof FacebookAuth) {
            if(resultCode == RESULT_OK)
				((FacebookAuth) authentication).getFacebookCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
        else if(authentication instanceof TwitterAuth) {
			((TwitterAuth) authentication).authClient.onActivityResult(requestCode, resultCode, data);
		}
    }

    public boolean isEditFilled() {
        return edtEmail.getText().toString().length() > 0 && edtPassword.getText().toString().length() > 0;
    }

    @Override
    public void onClick(View view) {
        if(!Util.hasInternetConnection(Login.this)) {
            Toast.makeText(Login.this, R.string.has_no_connection, Toast.LENGTH_SHORT).show();

            return;
        }

        mProgressDialog = ProgressDialog.show(Login.this, "Login", this.getString(R.string.executing_sign_in));

        switch (view.getId()) {

            case R.id.login_button_fb:
                authentication = new FacebookAuth(Login.this, Login.this, keyManager);
                authentication.login();
            break;

            case R.id.login_button_twitter:
                authentication = new TwitterAuth(Login.this, Login.this, keyManager);
                authentication.login();
            break;

            case R.id.login_button_google:
                authentication = new GoogleAuth(Login.this, Login.this, keyManager);
                authentication.login();
            break;

            case R.id.login_login_button:
                if (isEditFilled()) {
                    if(Util.isEmailValid(edtEmail.getText().toString())) {
                        authentication = new AppAuth(Login.this, edtEmail.getText().toString(),
                                					edtPassword.getText().toString(), Login.this);

                        authentication.login();
                    } else {
                        Toast.makeText(Login.this, getApplicationContext().getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                        dismissPDialog();
                    }
                } else {
                    dismissPDialog();
                    Toast.makeText(Login.this, getApplicationContext().getString(R.string.fill_login), Toast.LENGTH_SHORT).show();
                }
            break;
        }
    }

    private class LoginEditTextFieldFocusChangeListener implements View.OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {

			switch(v.getId()) {

				case R.id.login_email_field:
					SetEmailBlank(v, hasFocus);
				break;

				case R.id.login_password_field:
					SetPasswordBlank(v, hasFocus);
				break;
			}
		}

		private void SetEmailBlank(View v, boolean blank) {
			v.setBackground(getDrawable(blank ? R.drawable.login_email_blank : R.drawable.login_email));
		}

		private void SetPasswordBlank(View v, boolean blank) {
			v.setBackground(getDrawable(blank ? R.drawable.login_password_blank : R.drawable.login_password));
		}
	}

	private class LoginSelectorTouchListener implements View.OnTouchListener {

		private MotionEvent.PointerCoords coords;

		public LoginSelectorTouchListener() {
			coords = new MotionEvent.PointerCoords();
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			event.getPointerCoords(0, coords);
			Select(v, coords.x <= v.getWidth() * 0.5f);

			return true;
		}

		private void Select(View v, boolean binner) {
			((ImageView) v).setImageDrawable(getDrawable(binner ? R.drawable.login_binner_selected : R.drawable.login_resident_selected));
		}
	}
}
