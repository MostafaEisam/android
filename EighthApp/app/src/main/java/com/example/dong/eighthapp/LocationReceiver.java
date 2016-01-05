package com.example.dong.eighthapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by Dong on 2015-07-17.
 */
/*
UI 컴포넌트들 또는 애플리케이션 프로세스가 실행 중인지의 여부에 관계없이 받아야함
Broadcase Receiver가 가장 좋은 곳, 매니패스트에 등록되어야 한다.
자신이 받은 위치 갱신 정보를 로그에 수록할 것이다.
 */
public class LocationReceiver extends BroadcastReceiver {
    private  static final String TAG = "LocationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //인텐트 엑스트라로 반환된 위치 정보 데이터가 있으면 그것을 사용한다.
        Location loc = (Location)intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if(loc != null){
            onLocationReceived(context, loc);
            return;
        }

        //인텐트의 엑스트라로 반환된 위치 제공자의 사용 가능 여부가 있으면 그것을 사용한다.
        if(intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)){
            boolean enabled = intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
            onProviderEnabledChanged(enabled);
        }
    }

    //위치 제공자 이름, 위도, 경도를 로그에 기록
    protected void onLocationReceived(Context context, Location loc){
        Log.d(TAG, this + " Got location from " + loc.getProvider() + ": " + loc.getLatitude() + ", " + loc.getLongitude());
    }

    protected void onProviderEnabledChanged(boolean enabled){
        Log.d(TAG, "Provider " + (enabled ? "enabled" : "disabled"));
    }



}
