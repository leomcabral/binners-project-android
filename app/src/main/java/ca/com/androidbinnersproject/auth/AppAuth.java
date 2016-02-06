package ca.com.androidbinnersproject.auth;

import android.util.Log;

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
    private String LOG_TAG = getClass().getName();

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
                Log.i(LOG_TAG, "Backend login success!");

                if(response.code() == 200) {
                    onAuthListener.onLoginSuccess(response.body());
                } else {
                    onAuthListener.onLoginError("There is a problem on trying to login. Try again!");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "Backend login failure!");
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
