package ca.com.androidbinnersproject.clazz;

/**
 * Created by jonathan_campos on 18/01/2016.
 */
public abstract class Authentication {
    protected OnAuthListener onAuthListener;

    public abstract void login();

    public abstract void logout();

    public abstract void revoke();

    public void setOnAuthListener(OnAuthListener listener) {
        this.onAuthListener = listener;
    }
}
