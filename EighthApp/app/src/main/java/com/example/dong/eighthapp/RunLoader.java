package com.example.dong.eighthapp;

import android.content.Context;

/**
 * Created by Dong on 2015-07-20.
 */
public class RunLoader extends DataLoader<Run> {

    private long mRunId;

    public RunLoader(Context context, long runId){
        super(context);
        mRunId = runId;
    }

    @Override
    public Run loadInBackground() {
        return RunManager.getInstance(getContext()).getRun(mRunId);
    }
}
