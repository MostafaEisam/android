package com.example.dong.ewhakatalk_dongheekim.tabfragments;

import android.support.v4.app.Fragment;

/**
 * Created by manjong on 15. 5. 21..
 */
public abstract class BaseFragment extends Fragment {

    private String title;
    private String actionBarTitle;
    private int activatedIcon;
    private int defaultIcon;

    public int getActivatedIcon() {
        return activatedIcon;
    }

    public void setActivatedIcon(int activatedIcon) {
        this.activatedIcon = activatedIcon;
    }

    public int getDefaultIcon() {
        return defaultIcon;
    }

    public void setDefaultIcon(int defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActionBarTitle() {
        return actionBarTitle;
    }
}