
package ca.com.androidbinnersproject.util;

import android.util.Log;

/*
	Utility class to make it easier to output log messages
	Methods are ordered from least to most verbose

	Check Android docs for more info:
	http://developer.android.com/reference/android/util/Log.html
*/
public class Logger
{
	public static String DefaultTag = "BinnersApp";

	public static void Error(String message)
	{
		Error(DefaultTag, message, null);
	}

	public static void Error(String message, Throwable t)
	{
		Error(DefaultTag, message, t);
	}

	public static void Error(String tag, String message, Throwable t)
	{
		if(t != null)
			Log.e(tag, message, t);
		else
			Log.e(tag, message);
	}

	public static void Warn(String message)
	{
		Warn(DefaultTag, message, null);
	}

	public static void Warn(String message, Throwable t)
	{
		Warn(DefaultTag, message, t);
	}

	public static void Warn(String tag, String message, Throwable t)
	{
		if(t != null)
			Log.w(tag, message, t);
		else
			Log.w(tag, message);
	}

	public static void Info(String message)
	{
		Info(DefaultTag, message, null);
	}

	public static void Info(String message, Throwable t)
	{
		Info(DefaultTag, message, t);
	}

	public static void Info(String tag, String message, Throwable t)
	{
		if(t != null)
			Log.i(tag, message, t);
		else
			Log.i(tag, message);
	}

	public static void Debug(String message)
	{
		Debug(DefaultTag, message, null);
	}

	public static void Debug(String message, Throwable t)
	{
		Debug(DefaultTag, message, t);
	}

	public static void Debug(String tag, String message, Throwable t)
	{
		if(t != null)
			Log.d(tag, message, t);
		else
			Log.d(tag, message);
	}

	public static void Verbose(String message)
	{
		Verbose(DefaultTag, message, null);
	}

	public static void Verbose(String message, Throwable t)
	{
		Verbose(DefaultTag, message, t);
	}

	public static void Verbose(String tag, String message, Throwable t)
	{
		if(t != null)
			Log.v(tag, message, t);
		else
			Log.v(tag, message);
	}
}
