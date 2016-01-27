
package ca.com.androidbinnersproject.auth;

import android.app.Activity;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ca.com.androidbinnersproject.R;
import io.fabric.sdk.android.Fabric;

public class TwitterAuth extends Authentication
{
	private String consumerKey;
	private String consumerSecretKey;

	public TwitterAuthClient authClient;

	public TwitterAuth(Activity activity, OnAuthListener listener)
	{
		this.activity = activity;
		onAuthListener = listener;

		RetrieveKeys("keys/twitterKeys");

		TwitterAuthConfig authConfig = new TwitterAuthConfig(consumerKey, consumerSecretKey);
		Fabric.with(activity, new Twitter(authConfig));
	}

	private void RetrieveKeys(String keyFile)
	{
		String keyInfo;
		BufferedReader reader;

		try
		{
			reader = new BufferedReader(new InputStreamReader(activity.getAssets().open(keyFile)));
			keyInfo = reader.readLine();
			reader.close();
		}
		catch(IOException e)
		{
			//file won't be in repository, ask one of the collaborators if you need it
			Log.e(activity.getString(R.string.log_tag), "Failed to open TwitterKeys file: " + e.getMessage());
			return;
		}

		if(keyInfo.isEmpty())
			return;

		String[] keys = keyInfo.split(":");

		try
		{
			consumerKey = keys[0];
			consumerSecretKey = keys[1];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			Log.e(activity.getString(R.string.log_tag), "Failed to parse Twitter keys, check you keys file");
		}
	}

    @Override
    public void login()
    {
		authClient = new TwitterAuthClient();
		authClient.authorize(activity, new Callback<TwitterSession>()
		{
			@Override
			public void success(Result<TwitterSession> result)
			{
				Log.d(activity.getString(R.string.log_tag), "Twitter login successful");
			}

			@Override
			public void failure(TwitterException e)
			{
				Log.d(activity.getString(R.string.log_tag), "Twitter login failed");
			}
		});
    }

    @Override
    public void logout()
    {

    }

    @Override
    public void revoke()
    {

    }
}
