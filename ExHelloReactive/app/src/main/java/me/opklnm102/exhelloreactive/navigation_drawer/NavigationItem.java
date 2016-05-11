package me.opklnm102.exhelloreactive.navigation_drawer;

import android.graphics.drawable.Drawable;


public class NavigationItem {

    String mText;

    Drawable mDrawable;

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

    public NavigationItem(String text, Drawable drawable) {

        mText = text;
        mDrawable = drawable;
    }
}
