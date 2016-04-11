package me.opklnm102.exalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 알람
     * 미리 지정해 놓은 시간에 이벤트를 발생시키는 시스템 장치
     * 장래의 특정 시점이나 일정시간 경과 후에 할 작업을 등록하고 싶을 때 사용한다.
     * OS가 관리. App 외부에서도 설정 가능
     * App이 종료된 상태에서도 동작
     * 시간이 되면 App을 기동 - 슬립상태여도 깨워 App을 실행
     * 전원을 끄거나 명시적으로 알람을 취소하지 않는 한 어떤 조건에서도 정확하게 동작
     * ex) 모닝콜, 약속시간 알림, 예약 다운로드, 주기적으로 해야할 작업(이미지 파일 인덱싱, 바이러스 체크, 조각모음)
     *
     * AlarmManager클래스로 관리
     * 시스템 서비스
     *
     * 알람 등록 메소드
     *
     *  1. 한번만 동작하는 알람
     *      void set(int type, long triggerAtTime, PendingIntent operation)
     *  2. 주기를 정해놓고 반복적으로 동작하는 알람
     *      void setRepeating(int type, long triggerAtTime, long interval, PendingIntent operation)
     *
     *  type - 예약시간을 해석하는 방법, 예약시간에 장비가 슬랩 모드일때 장비의 기동여부 지정
     *      경과 시간 이용 - 현재 시간을 기준으로해서 일정 시간후에 알람을 등록
     *      세계 표준시 이용 - 절대 시간으로 등록
     *
     *      RTC - System.currentTimeMillis()로 구한 셰계 표준시(UTC)로 지정
     *      RTC_WAKEUP - 위와 같되 장비를 깨운다.
     *      ELAPSED_REALTIME - SystemClock.elapsedRealtime()로 구한 부팅된 이후의 경과시간으로 지정
     *      ELAPSED_REALTIME_WAKEUP - 위와 같되 장비를 깨운다.
     *
     *  triggerAtTime - 알람을 기동할 시간을 지정, type에 따라 포맷이 다름
     *      알람시간은 미래의 시간, 과거시간 등록시 즉시 동작
     *
     *  operation - 예약 시간이 되었을 때 수행할 작업을 지정
     *      예약시간이 되었을 때 수행할 작업을 지정하는 펜딩 인텐트. 호출할 컴포넌트에 따라 생성 메소드가 다르다.
     *
     *  예약시간이 되었을 때 액티비티, Broadcast, 서비스 기동
     *  알람에서 액티비티, 서비스를 직접 실행하는 경우는 흔치 않으며 보통 Broadcast를 통해 알람 시간이 되었음을 알리기만 한다.
     *  알람의 고유기능은 시간이 되었음을 알리는 것이므로 BR를 호출하는 것이 용도상 가장 적합
     *  알람이 시간을 알려주면 BR가 미리 정의된 동작 수행
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnOneTime = (Button) findViewById(R.id.oneTime);
        Button btnRepeat = (Button) findViewById(R.id.repeat);
        Button btnStop = (Button) findViewById(R.id.stop);

        btnOneTime.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent;
        PendingIntent sender;

        switch (v.getId()) {
            case R.id.oneTime:
                //약속시간 알림 기능
                // 현재 시점 이후 일정 시간이 경과 했을 때 BR 호출

                intent = new Intent(MainActivity.this, AlarmReceiver.class);
                sender = PendingIntent.getBroadcast(this, 0, intent, 0);

                //알람시간. 10초후
                //실제 약속 시간은 6월 29일 오후 7:30분 식으로 절대 시간으로 지정하는 것이 보통이며 이런 절대시간을 지정할 때는 RTC타입이 적당
                //절대시간으로 지정하려면 calendar객체의 set메소드로 년월일시분초를 설정
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.SECOND, 10);

                //알람등록
                am.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);
                break;
            case R.id.repeat:
                intent = new Intent(MainActivity.this, DisplayScoreReceiver.class);
                sender = PendingIntent.getBroadcast(this, 0, intent, 0);

                //6초당 1번 알람 등록
                am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 6000, sender);
                break;
            case R.id.stop:
                intent = new Intent(MainActivity.this, DisplayScoreReceiver.class);
                sender = PendingIntent.getBroadcast(this, 0, intent, 0);

                am.cancel(sender);
                break;
        }

    }
}
