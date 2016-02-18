package ca.com.androidbinnersproject.apis;

import ca.com.androidbinnersproject.models.Profile;
import ca.com.androidbinnersproject.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jonathan_campos on 18/02/2016.
 */
public interface CreateUserService {
    @POST("/users")
    public abstract Call<Profile> create(@Body User user);
}
