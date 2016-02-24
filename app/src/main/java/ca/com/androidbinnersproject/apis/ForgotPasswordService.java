
package ca.com.androidbinnersproject.apis;

import ca.com.androidbinnersproject.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ForgotPasswordService {
	@GET("auth/forgot/{email}")
	Call<User> forgotPassword(@Path("email") String email);
}
