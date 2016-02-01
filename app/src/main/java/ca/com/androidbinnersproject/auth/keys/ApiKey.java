
package ca.com.androidbinnersproject.auth.keys;

public class ApiKey
{
	private String key;
	private String secret;

	public ApiKey(String key, String secret)
	{
		this.key = key;
		this.secret = secret;
	}

	public String GetKey()
	{
		return key;
	}

	public String GetSecret()
	{
		return secret;
	}
}
