package ca.com.androidbinnersproject.apis;

import ca.com.androidbinnersproject.models.Profile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jonathan_campos on 28/01/2016.
 */
public interface FacebookLoginService {

    @GET("auth/facebook/{accessToken}")
    public Call<Profile> authenticate(@Path("accessToken") String token);
}
