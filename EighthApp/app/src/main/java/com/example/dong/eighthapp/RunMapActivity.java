package com.example.dong.eighthapp;

import android.support.v4.app.Fragment;

/**
 * Created by Dong on 2015-07-20.
 */
public class RunMapActivity extends SingleFragmentActivity {
    public static final String EXTRA_RUN_ID = "com.example.dong.eighthapp.run_id";

    @Override
    protected Fragment createFragment() {
        long runId = getIntent().getLongExtra(EXTRA_RUN_ID, -1);
        if(runId != -1){
            return RunMapFragment.newInstance(runId);
        }else{
            return new RunMapFragment();
        }
    }
}
