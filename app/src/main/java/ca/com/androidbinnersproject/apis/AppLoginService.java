package ca.com.androidbinnersproject.apis;

import ca.com.androidbinnersproject.auth.Profile;
import ca.com.androidbinnersproject.auth.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jonathan_campos on 28/01/2016.
 */

public interface AppLoginService {
    @POST("auth")
    Call<Profile> authenticate(@Body User user);
}
