
package ca.com.androidbinnersproject.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util
{
	public static byte[] HexStringToByteArray(String s)
	{
		int len = s.length();
		byte[] data = new byte[len / 2];

		for(int i = 0; i < len; i += 2)
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));

		return data;
	}

	public static boolean hasInternetConnection(Context context) {
		ConnectivityManager cm =
				(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null &&
				activeNetwork.isConnectedOrConnecting();
	}
}
