package com.example.dong.eighthapp;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Dong on 2015-07-20.
 */
/*
main쓰레드에서 DB를 쿼리하지 않도록 Loader사용
 */
public class LocationListCursorLoader extends SQLiteCursorLoader {
    private long mRunId;

    public LocationListCursorLoader(Context context, long runId){
        super(context);
        mRunId = runId;
    }

    @Override
    protected Cursor loadCursor() {
        return RunManager.getInstance(getContext()).queryLocationsForRun(mRunId);
    }
}
