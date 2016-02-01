package ca.com.androidbinnersproject.apis;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by jonathan_campos on 28/01/2016.
 */
public class BaseAPI {
    private static final String ENDPOINT = "http://dev-b.leomcabral.com:80/api/v1.0/";

    private static Retrofit retrofit;

    public static Retrofit getRetroInstance() {
        if(retrofit != null) {
            return retrofit;
        }
        return retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
