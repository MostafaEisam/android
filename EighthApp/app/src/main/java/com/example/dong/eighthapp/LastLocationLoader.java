package com.example.dong.eighthapp;

import android.content.Context;
import android.location.Location;

/**
 * Created by Dong on 2015-07-20.
 */
public class LastLocationLoader extends DataLoader<Location> {
    private long mRunId;

    public LastLocationLoader(Context context, long runId){
        super(context);
        mRunId = runId;
    }

    @Override
    public Location loadInBackground() {
        return RunManager.getInstance(getContext()).getLastLocationForRun(mRunId);
    }
}
