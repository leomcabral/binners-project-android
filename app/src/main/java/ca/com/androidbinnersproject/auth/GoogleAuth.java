
package ca.com.androidbinnersproject.auth;

import android.accounts.Account;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import java.io.IOException;

import ca.com.androidbinnersproject.R;
import ca.com.androidbinnersproject.apis.BaseAPI;
import ca.com.androidbinnersproject.apis.GoogleLoginService;
import ca.com.androidbinnersproject.auth.keys.KeyManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoogleAuth extends Authentication implements OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    public static final int GOOGLE_SIGN_IN = 10;
    final String LOG_TAG = getClass().getName();

    GoogleSignInOptions mGso;


    final Profile mProfile = new Profile();

    public GoogleAuth(AppCompatActivity activity, OnAuthListener listener, KeyManager keyManager) {
        this.activity = activity;

        mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGso)
                .build();

		if(listener != null)
        	setOnAuthListener(listener);
    }

    @Override
    public void login() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void logout() {

    }

    @Override
    public void revoke() {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            /*String scopes = "oauth2:profile email";

            Account androidAccount = new Account(acct.getDisplayName(), "");

            String token = "";

            try {
                token = GoogleAuthUtil.getToken(activity, androidAccount, scopes);
            } catch (IOException | GoogleAuthException e ) {

            }*/

            signInBackend(acct.getDisplayName(), acct.getEmail(), acct.getIdToken());
        } else {
            Log.e(LOG_TAG, "Google login failure!");
            onAuthListener.onLoginError("Error on trying to log at the backend.");
        }
    }

    private void signInBackend(final String name, final String email, String accessToken) {
        Retrofit retrofit = BaseAPI.getRetroInstance();

        GoogleLoginService service = retrofit.create(GoogleLoginService.class);

        Call<Profile> call = service.authenticate(accessToken);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Response<Profile> response) {
                Log.i(LOG_TAG, "Backend login success!");
                Profile profile = response.body();
                profile.setName(name);
                profile.setEmail(email);

                onAuthListener.onLoginSuccess(profile);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "Backend login failure!");
                onAuthListener.onLoginError("Error on trying to log at the backend.");
            }
        });
    }

    /*private class AccessTokenRequest extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) {
            String token = null;
            final String SCOPES = "https://www.googleapis.com/auth/plus.login ";

            try {
                token = GoogleAuthUtil.getToken(
                        activity,
                        Plus.AccountApi.getAccountName(mGoogleApiClient),
                        "oauth2:" + SCOPES);
            } catch (IOException | GoogleAuthException e) {
                Log.e(LOG_TAG, "Error after trying to get access token");
            }

            return token;
        }

        @Override
        protected void onPostExecute(String token) {
            Log.i(LOG_TAG, "Google access token retrieved!");
            mProfile.setToken(token);

            Retrofit retrofit = BaseAPI.getRetroInstance();

            GoogleLoginService service = retrofit.create(GoogleLoginService.class);

            Call<Profile> call = service.authenticate(token);

            call.enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Response<Profile> response) {
                    Log.i(LOG_TAG, "Backend login success!");
                    onAuthListener.onLoginSuccess(mProfile);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(LOG_TAG, "Backend login failure!");
                    onAuthListener.onLoginError("Error on trying to log at the backend.");
                }
            });
        }
    }*/
}
