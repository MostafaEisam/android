package com.example.dong.eighthapp;

import android.content.Context;
import android.location.Location;

/**
 * Created by Dong on 2015-07-17.
 */
/*
App이 실행 중인지의 여부에 관계없이 우리의 위치 데이터를 갖는 인텐트가 반드시 처리된다.
-> LocationReceiver의 서브 클래스이고 인텐트 필터를 통해 매니패스트에 등록 필요
back 버튼을 눌러서 App이 죽더라도 이동을 계속 추적 기록할 수 있다.
 */
public class TrackingLocationReceiver extends LocationReceiver {

    @Override
    protected void onLocationReceived(Context context, Location loc) {
        RunManager.getInstance(context).insertLocation(loc);
    }
}
