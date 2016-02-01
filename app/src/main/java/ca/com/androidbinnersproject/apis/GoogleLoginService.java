package ca.com.androidbinnersproject.apis;

import ca.com.androidbinnersproject.auth.Profile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jonathan_campos on 28/01/2016.
 */
public interface GoogleLoginService {

    @GET("auth/google/{accessToken}")
    public Call<Profile> authenticate(@Path("accessToken") String token);
}
