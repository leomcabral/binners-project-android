
package ca.com.androidbinnersproject.auth.keys;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.tozny.crypto.android.AesCbcWithIntegrity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import ca.com.androidbinnersproject.R;
import ca.com.androidbinnersproject.util.Logger;

import static com.tozny.crypto.android.AesCbcWithIntegrity.CipherTextIvMac;
import static com.tozny.crypto.android.AesCbcWithIntegrity.SecretKeys;

public class KeyManager
{
	private Resources appResources;

	private ApiKey facebookApiKey;
	private ApiKey googleApiKey;
	private ApiKey twitterApiKey;

	public KeyManager(@NonNull Resources appResources)
	{
		this.appResources = appResources;
	}

	public ApiKey GetFacebookApiKey()
	{
		return facebookApiKey;
	}

	public ApiKey GetGoogleApiKey()
	{
		return googleApiKey;
	}

	public ApiKey GetTwitterApiKey()
	{
		return twitterApiKey;
	}

	public boolean RetrieveKeys()
	{
		String keyInfo;

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(appResources.getAssets().open("keys/apiKeys")));
			keyInfo = reader.readLine();
			reader.close();
		}
		catch(IOException e)
		{
			Logger.Error("Failed to open APIKeys file", e);
			return false;
		}

		int cryptKeyId = appResources.getIdentifier("cryptKey", "string", appResources.getString(R.string.packageName));
		int saltId = appResources.getIdentifier("salt", "string", appResources.getString(R.string.packageName));

		if(cryptKeyId == 0 || saltId == 0)
		{
			Logger.Info("APIKeys key resources was not found, ask a collaborator if you really need it");
			return false;
		}

		String cryptKey = appResources.getString(cryptKeyId);
		String salt = appResources.getString(saltId);

		SecretKeys keys = null;

		try
		{
			keys = AesCbcWithIntegrity.generateKeyFromPassword(cryptKey, salt);
		}
		catch(GeneralSecurityException e)
		{
			Logger.Error("Failed to create keys", e);
		}

		if(keys == null)
		{
			Logger.Error("Keys are null");
			return false;
		}

		CipherTextIvMac cipherText = new CipherTextIvMac(keyInfo);
		String plainData = "FAILED";

		try
		{
			plainData = AesCbcWithIntegrity.decryptString(cipherText, keys);
		}
		catch(GeneralSecurityException | UnsupportedEncodingException e)
		{
			Logger.Error("Failed to decrypt", e);
			return false;
		}

		if(plainData.contentEquals("FAILED"))
		{
			Logger.Error("Failed to decrypt, probably an integrity issue");
			return false;
		}

		//All this effort just to fill up RAM with the plain data

		ParseKeys(plainData);

		return true;
	}

	private void ParseKeys(@NonNull String keyData)
	{
		BufferedReader reader = new BufferedReader(new StringReader(keyData));
		String line;

		try
		{
			while((line = reader.readLine()) != null)
			{
				String[] keyPieces = line.split(":");

				if(keyPieces[0].contentEquals("facebook"))
					facebookApiKey = new ApiKey(keyPieces[1], keyPieces[2]);
				else if(keyPieces[0].contentEquals("google"))
					googleApiKey = new ApiKey(keyPieces[1], keyPieces[2]);
				else if(keyPieces[0].contentEquals("twitter"))
					twitterApiKey = new ApiKey(keyPieces[1], keyPieces[2]);
				else
					Logger.Info("Strange line found: " + keyPieces[0]);
			}
		}
		catch(IOException | ArrayIndexOutOfBoundsException e)
		{
			Logger.Error("Failed reading keys file content", e);
		}
	}
}
