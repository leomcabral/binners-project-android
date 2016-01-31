package ca.com.androidbinnersproject.auth;

import ca.com.androidbinnersproject.apis.AppLoginService;
import ca.com.androidbinnersproject.apis.BaseAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by jonathan_campos on 18/01/2016.
 */
public class AppAuth extends Authentication {

    private User user;

    public AppAuth(String email, String password, OnAuthListener listener) {
        user = new User();
        user.setEmail(email);
        user.setPassword(password);

        if(listener != null) {
            setOnAuthListener(listener);
        }
    }

    @Override
    public void login() {
        Retrofit retrofit = BaseAPI.getRetroInstance();

        AppLoginService service = retrofit.create(AppLoginService.class);

        Call<Profile> call = service.authenticate(user);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Response<Profile> response) {
                onAuthListener.onLoginSuccess(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void logout() {

    }

    @Override
    public void revoke() {

    }
}
