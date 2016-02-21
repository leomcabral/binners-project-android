package ca.com.androidbinnersproject.listeners;

/**
 * Created by jonathan_campos on 21/02/2016.
 */
public interface ResponseListener<T> {

    void onSuccess(T type);

    void onFailed(String message);
}
