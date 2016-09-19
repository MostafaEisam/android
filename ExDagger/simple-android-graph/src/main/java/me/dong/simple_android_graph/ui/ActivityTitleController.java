package me.dong.simple_android_graph.ui;

import android.app.Activity;

import javax.inject.Inject;

import me.dong.simple_android_graph.PreActivity;

/**
 * A simple abstraction which provides the ability to set the title on an activity.
 * <p/>
 * Fragments should not directly modify any part of an activity outside of the view or dialog that
 * it creates. This class provides a way for fragments to inject a controller that will allow for
 * control of the activity title. While not exceedingly useful in practice, this concept could be
 * expanded to things like facilitating control over the action bar, dialogs, notifications, etc.
 */
@PreActivity
public class ActivityTitleController {
    private final Activity mActivity;

    @Inject
    public ActivityTitleController(Activity activity){
        this.mActivity = activity;
    }

    public void setTitle(CharSequence title){
        mActivity.setTitle(title);
    }
}
