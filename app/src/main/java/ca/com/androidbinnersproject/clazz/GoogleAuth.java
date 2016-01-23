package ca.com.androidbinnersproject.clazz;

import android.app.Activity;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;

/**
 * Created by jonathan_campos on 18/01/2016.
 */
public class GoogleAuth extends Authentication implements ConnectionCallbacks, OnConnectionFailedListener {

    private Activity mActivity;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;

    private static final int RC_SIGN_IN = 0;

    public static final int GOOGLE_SIGN_IN = 101010;
    final String LOG_TAG = getClass().getName();

    final Profile mProfile = new Profile();

    public GoogleAuth(Activity activity, OnAuthListener listener) {
        mActivity = activity;

        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        setOnAuthListener(listener);
    }

    @Override
    public void login() {
        if(!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void logout() {
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void revoke() {
        if(mGoogleApiClient.isConnected()) {
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            onAuthListener.onRevoke();
                        }
                    }
            );
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        mProfile.setEmail(Plus.AccountApi.getAccountName(mGoogleApiClient));
        mProfile.setName(person.getDisplayName());

        AccessTokenRequest tokenRequest = new AccessTokenRequest();
        tokenRequest.execute();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()) {
            try {
                mActivity.startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),
                        GOOGLE_SIGN_IN, null, 0, 0, 0);
            }catch (IntentSender.SendIntentException e) {
                Log.e(LOG_TAG, e.getMessage());
                mGoogleApiClient.connect();
            }
        } else{
            String message = "Google Plus Error: "+ connectionResult.getErrorCode();
            Log.e(LOG_TAG, message);

            onAuthListener.onLoginError(message);
        }
    }

    private class AccessTokenRequest extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) {
            String token = null;
            final String SCOPES = "https://www.googleapis.com/auth/plus.login ";

            try {
                token = GoogleAuthUtil.getToken(
                        mActivity,
                        Plus.AccountApi.getAccountName(mGoogleApiClient),
                        "oauth2:" + SCOPES);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error after trying to get access token" + token);
            } catch (GoogleAuthException e) {
                Log.e(LOG_TAG, "Error after trying to get access token" + token);
            }


            return token;

        }

        @Override
        protected void onPostExecute(String token) {
            Log.i(LOG_TAG, "Access token retrieved:" + token);
            mProfile.setAccessToken(token);
            onAuthListener.onLoginSuccess(mProfile);
        }

    }
}
