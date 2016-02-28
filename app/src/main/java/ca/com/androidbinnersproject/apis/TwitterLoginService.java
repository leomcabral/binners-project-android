
package ca.com.androidbinnersproject.apis;

import ca.com.androidbinnersproject.models.Profile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TwitterLoginService {
	@GET("auth/twitter/{accessToken}/{accessSecret}")
	Call<Profile> authenticate(@Path("accessToken") String accessToken, @Path("accessSecret") String accessSecret);
}
