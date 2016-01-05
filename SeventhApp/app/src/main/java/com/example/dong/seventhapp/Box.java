package com.example.dong.seventhapp;

import android.graphics.PointF;

/**
 * Created by Dong on 2015-07-15.
 */
public class Box {
    private PointF mOrigin;  //시작 지점
    private PointF mCurrent;  //현재 지점

    public Box(PointF origin){
        mOrigin = mCurrent = origin;
    }

    public void setCurrent(PointF current){
        mCurrent = current;
    }

    public PointF getCurrent(){
        return mCurrent;
    }

    public PointF getOrigin(){
        return mOrigin;
    }
}
