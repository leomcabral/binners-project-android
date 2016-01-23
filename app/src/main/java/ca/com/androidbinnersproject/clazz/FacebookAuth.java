package ca.com.androidbinnersproject.clazz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by jonathan_campos on 18/01/2016.
 */
public class FacebookAuth extends Authentication implements FacebookCallback<LoginResult>{

    private final Activity mActivity;
    private final CallbackManager facebookCallbackManager;
    private final LoginManager facebookLoginManager;
    private String LOG_TAG = getClass().getName();

    public FacebookAuth(Activity activity, OnAuthListener listener) {
        FacebookSdk.sdkInitialize(activity.getApplicationContext());

        mActivity = activity;
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginManager = LoginManager.getInstance();
        facebookLoginManager.registerCallback(facebookCallbackManager, this);

        setOnAuthListener(listener);
    }

    @Override
    public void login() {
        facebookLoginManager.logInWithReadPermissions(mActivity,
                Arrays.asList("public_profile", "email"));
    }

    @Override
    public void logout() {
        facebookLoginManager.logOut();
    }

    @Override
    public void revoke() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if(accessToken != null) {
            String user = accessToken.getUserId();

            //url to revoke login DELETE {user_id}/permissions
            String graphPath = user + "/permissions";

            final GraphRequest requestLogout = new GraphRequest(accessToken, graphPath, null, HttpMethod.DELETE, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    //call logout to clear token info and profile
                    logout();

                    onAuthListener.onRevoke();
                }
            });

            requestLogout.executeAsync();
        }else{
            onAuthListener.onLoginError("Token is null");
        }
    }

    @Override
    public void onSuccess(final LoginResult loginResult) {
        GraphRequest meRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                String name = "";
                String email = "";
                String accessToken = loginResult.getAccessToken().getToken();

                try {
                    name = jsonObject.getString("name");
                    email = jsonObject.getString("email");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }

                Profile profile = new Profile();
                profile.setName(name);
                profile.setEmail(email);
                profile.setAccessToken(accessToken);

                onAuthListener.onLoginSuccess(profile);

                //requestProfilePhoto(name, email, cover);
            }
        });

        Bundle params = new Bundle();
        params.putString("fields", "name, email, cover");

        meRequest.setParameters(params);
        meRequest.executeAsync();
    }

    @Override
    public void onCancel() {
        onAuthListener.onLoginCancel();
    }

    @Override
    public void onError(FacebookException error) {
        Log.e(LOG_TAG, error.getMessage());
        onAuthListener.onLoginError(error.getMessage());
    }

    public CallbackManager getFacebookCallbackManager() {
        return facebookCallbackManager;
    }
}
