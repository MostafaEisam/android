package com.example.dong.sixthapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Dong on 2015-07-14.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "received result: " + getResultCode());
        if(getResultCode() != Activity.RESULT_OK)
            //포그라운드에서 실행되는 액티비티가 브로드캐스트를 취소하였다.
        return;

        int requestCode = intent.getIntExtra("REQUEST_CODE", 0);
        Notification notification = (Notification)intent.getParcelableExtra("NOTIFICATION");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, notification);
    }
}
