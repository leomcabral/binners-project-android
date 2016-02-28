package ca.com.androidbinnersproject.auth;

import android.app.Activity;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import ca.com.androidbinnersproject.apis.BaseAPI;
import ca.com.androidbinnersproject.apis.TwitterLoginService;
import ca.com.androidbinnersproject.auth.keys.ApiKey;
import ca.com.androidbinnersproject.auth.keys.KeyManager;
import ca.com.androidbinnersproject.listeners.OnAuthListener;
import ca.com.androidbinnersproject.models.Profile;
import ca.com.androidbinnersproject.models.User;
import ca.com.androidbinnersproject.util.Logger;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TwitterAuth extends Authentication {

	public TwitterAuthClient authClient;
	private TwitterSession session;

	public TwitterAuth(Activity activity, OnAuthListener listener, KeyManager keyManager) {
		this.activity = activity;
		onAuthListener = listener;

		ApiKey twitterApiKey = keyManager.GetTwitterApiKey();

		TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterApiKey.GetKey(), twitterApiKey.GetSecret());
		Fabric.with(activity, new Twitter(authConfig));
	}

	@Override
	public void login() {
		authClient = new TwitterAuthClient();
		authClient.authorize(activity, new Callback<TwitterSession>() {
			@Override
			public void success(Result<TwitterSession> result) {

				Logger.Info("Twitter login successful");

				session = result.data;
				signInBackend();
			}

			@Override
			public void failure(TwitterException e) {
				Logger.Info("Twitter login failed");
			}
		});
	}

	private void signInBackend() {
		if(session == null)
			return;

		TwitterAuthToken token = session.getAuthToken();

		Retrofit retrofit = BaseAPI.getRetroInstance();
		TwitterLoginService service = retrofit.create(TwitterLoginService.class);

		Call<Profile> call = service.authenticate(token.token, token.secret);

		call.enqueue(new retrofit2.Callback<Profile>() {
			@Override
			public void onResponse(Response<Profile> response) {
				if(response.isSuccess())
					Logger.Info("Twitter login successful");
			}

			@Override
			public void onFailure(Throwable t) {
				Logger.Info("Twitter login failed");
			}
		});
	}

	@Override
	public void logout() {
	}

	@Override
	public void revoke() {
	}
}
