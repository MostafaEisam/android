package com.example.dong.sixthapp;

import android.support.v4.app.Fragment;

/**
 * Created by Dong on 2015-07-15.
 */
public class PhotoPageActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PhotoPageFragment();
    }
}
