package com.example.dong.eighthapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Dong on 2015-07-19.
 */
/*
AsyncTaskLoader API를 구현하여 mCursor인스턴스 변수의 Cursor객체를 효율적으로 로드하고 저장
 */
public abstract class SQLiteCursorLoader extends AsyncTaskLoader<Cursor>{

    private Cursor mCursor;

    public SQLiteCursorLoader(Context context) {
        super(context);
    }

    protected abstract Cursor loadCursor();

    /*
    Cursor객체를 얻기 휘해 추상 메소드 loadCursor()를 호출
    main쓰레드에 전달될 때 데이터가 메모리에서 사용가능한지 확인하기 위해 getCount()호출
     */
    @Override
    public Cursor loadInBackground() {

        Cursor cursor = loadCursor();
        if(cursor != null){
            cursor.getCount();
        }
        return cursor;
    }

    /*
    만일 로더가 시작되었으면(데이터가 전달될 수 있다는 의미) super클래스의 구현 버전이 호출
    만일 이전 커서가 더이상 필요 없으면 그것의 리소스를 해제하기 위해 닫는다.
    기존커서가 캐시에 있어 다시 전달될 수 있으므로, 이전 커서를 닫기 전에 이전 커서와 새커서가
    같은지를 확인하는 것이 중요!!
     */
    @Override
    public void deliverResult(Cursor data) {
        Cursor oldCursor = mCursor;
        mCursor = data;

        if(isStarted()){
            super.deliverResult(data);
        }

        if(oldCursor != null && oldCursor != data && !oldCursor.isClosed()){
            oldCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if(mCursor != null){
            deliverResult(mCursor);
        }
        if(takeContentChanged() || mCursor == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        //가능하다면 현재 로드 작업의 취소를 시도한다.
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        //로더가 중단되었는지 확인한다.
        onStopLoading();

        if(mCursor != null && !mCursor.isClosed()){
            mCursor.close();
        }
        mCursor = null;
    }
}
