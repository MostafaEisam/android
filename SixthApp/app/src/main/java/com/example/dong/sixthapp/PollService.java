package com.example.dong.sixthapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Dong on 2015-07-14.
 */
public class PollService extends IntentService {
    private static final String TAG = "PollService";

    private static final int POLL_INTERVAL = 1000 * 60 * 5;  //5분
    public static final String PREF_IS_ALARM_ON = "isAlarmOn";

    public static final String ACTION_SHOW_NOTIFICATION = "com.example.dong.sixthApp.SHOW_NOTIFICATION";

    public static final String PERM_PRIVATE = "com.example.dong.sixthApp.PRIVATE";

    public PollService() {
        super(TAG);
    }

    //자동으로 호출
    @Override
    protected void onHandleIntent(Intent intent) {

        //백그라운드 네트워크 사용 가능 여부 확인
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        //4.0(아센)부터 백그라운드 데이터 설정으로 네트워크 전체를 사용할 수 없도록 변경
                //getActiveNetworkInfo가 null이면 네트워킹은 전혀 동작하지 않는다.
                boolean isNetworkAvailable = cm.getBackgroundDataSetting() && cm.getActiveNetworkInfo() != null;
        if (!isNetworkAvailable) return;

        Log.i(TAG, "Received an intent: " + intent);

        //디폴트 SharedPreferences에 저장되어 있는 가장 최근의 쿼리와 결과 데이터 ID를 읽는다.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String query = prefs.getString(FlickrFetchr.PREF_SEARCH_QUERY, null);
        String lastResultId = prefs.getString(FlickrFetchr.PREF_LAST_RESULT_ID, null);

        //가장최근의 결과를 쿼리하여 가져온다.
        ArrayList<GalleryItem> items;
        if (query != null) {
            items = new FlickrFetchr().search(query);
        } else {
            items = new FlickrFetchr().fetchItems();
        }

        if (items.size() == 0)
            return;

        //만일 쿼리 결과가 있으면 1번째것의 ID를 알아낸다.
        String resultId = items.get(0).getId();

        //그것이 1번의 가장 최근결과 ID와 다른지 확인하고 로그 메시지로 수록
        if (!resultId.equals(lastResultId)) {
            Log.i(TAG, "Got a new result: " + resultId);

            Resources r = getResources();
            PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, PhotoGalleryActivity.class), 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(r.getString(R.string.new_pictures_text))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(r.getString(R.string.new_pictures_text))
                    .setContentText(r.getString(R.string.new_pictures_text))
                    .setContentIntent(pi)
                    .setAutoCancel(true)  //선택시 통지함에서 자동 삭제
                    .build();

            //NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            //정수 인자는 통지의 식별자, App에서 고유해야 한다.
            //이것과 같은 두번째 통지를 게시하면 교체된다.
            //notificationManager.notify(0, notification);

            //브로드캐스트 인텐트 전송
            //sendBroadcast(new Intent(ACTION_SHOW_NOTIFICATION));

            //브로드캐스트 인텐트 전송, 퍼미션 전달
            //sendBroadcast(new Intent(ACTION_SHOW_NOTIFICATION), PERM_PRIVATE);
            showBackgroundNotification(0, notification);
        } else {
            Log.i(TAG, "Got an old result: " + resultId);
        }

        //첫번째 ID를 가장 최근 결과ID로 SharedPreferences에 다시 저장
        prefs.edit()
                .putString(FlickrFetchr.PREF_LAST_RESULT_ID, resultId)
                .commit();
    }

    //알람을 켜거나 끈다.
    public static void setServiceAlarm(Context context, boolean isOn) {
        //PollService를 시작시키는 PendingIntent를 구성
        Intent intent = new Intent(context, PollService.class);
        //PendingIntent.getService(Context, requestCode, Intent, Flags);
        //Flags PendingIntent가 생성되는 방법을 변경하기 위해 사용할 수 있다.
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            //알람을 설정
            //알람의 기준시간, 최초 동작시간, 반복시간, 알람동작시 촉발시킬 PendingIntent
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pi);
        } else {
            //알람 취소
            alarmManager.cancel(pi);
            pi.cancel();
        }

        //알람상태를 프리퍼런스에 추가
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PollService.PREF_IS_ALARM_ON, isOn)
                .commit();
    }

    //알람이 켜져있는지 여부를 알려주기
    //이 PendingIntent는 알람의 설정에만 사용되므로, null이라는 것은 알람이 설정되지 않았다는 것
    public static boolean isServiceAlarmOn(Context context) {
        Intent intent = new Intent(context, PollService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    //순차 브로드캐스트 전송하기
    //Notification호출을 래핑하여 브로드캐스트로 외부에 전송
    //래핑된 Notification을 외부로 전송하기 위해 결과 수신자가 지정
    void showBackgroundNotification(int requestCode, Notification notification){
        Intent intent = new Intent(ACTION_SHOW_NOTIFICATION);
        intent.putExtra("REQUEST_CODE", requestCode);
        intent.putExtra("NOTIFICATION", notification);

        //sendOrderedBroadcast(Intent, String, BroadcastReceiver, Hnadler, int, String, Bundle);
        //(촉발시킬 인텐트,퍼미션,결과 수신자, 수신자를 실행할 Handler, 결과 코드의 초기값, 결과 데이터, 순차 브로드캐스트 인텐트의 결과 엑스트라(Bundle))
        sendOrderedBroadcast(intent, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }
}
