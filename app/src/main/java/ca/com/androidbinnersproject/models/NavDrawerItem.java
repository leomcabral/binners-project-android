package ca.com.androidbinnersproject.models;

/**
 * Created by jonathan_campos on 12/03/2016.
 */
public class NavDrawerItem {
    private boolean showNotify;
    private String title;

    public NavDrawerItem(){}

    public NavDrawerItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.title = title;
    }

    public boolean isShowNotify() {
        return this.showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
