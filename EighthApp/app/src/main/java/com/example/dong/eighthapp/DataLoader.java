package com.example.dong.eighthapp;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Dong on 2015-07-20.
 */
/*
간단한 데이터 로더
로드할 데이터가 어떤 타입이건 그것의 인스턴스를 수용하기위해 제네릭 타입 D를 사용
 */
public abstract class DataLoader<D> extends AsyncTaskLoader<D> {
    private D mData;

    public DataLoader(Context context){
        super(context);
    }

    //데이터가 있는지 확인
    //있으면 바로 전달, 없으면 forceLoad()를 호출해 데이터를 가져온다.
    @Override
    protected void onStartLoading() {
        if(mData != null){
            deliverResult(mData);
        }else{
            forceLoad();
        }
    }

    @Override
    public void deliverResult(D data) {
        mData = data;
        if(isStarted())
            super.deliverResult(data);
    }
}
