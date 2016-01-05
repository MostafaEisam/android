package com.example.dong.eighthapp;

import android.support.v4.app.Fragment;

/**
 * Created by Dong on 2015-07-17.
 */
public class RunListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RunListFragmnet();
    }
}
