package ca.com.androidbinnersproject.clazz;

/**
 * Created by jonathan_campos on 18/01/2016.
 */
public interface OnAuthListener {

    /**
     * Called when the login was succefully made and allow to save
     * some users information.
     * @param profile Information about the user eg. Name
     */
    void onLoginSuccess(Profile profile);

    /**
     * If some error happen this method is gonna be called.
     * @param message
     */
    void onLoginError(String message);

    /**
     *
     */
    void onLoginCancel();

    /**
     * The user has revoked the App's access to his information.
     */
    void onRevoke();
}
