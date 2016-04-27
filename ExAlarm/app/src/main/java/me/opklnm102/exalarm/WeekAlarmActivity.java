package me.opklnm102.exalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import java.util.Calendar;

//선택한 요일, 24시간마다 반복하는 알람
public class WeekAlarmActivity extends AppCompatActivity {

    public static final String TAG = WeekAlarmActivity.class.getSimpleName();

    AlarmManager am;

    Button btnRegister;
    Button btnUnRegister;

    ToggleButton tBtnSun;
    ToggleButton tBtnMon;
    ToggleButton tBtnTue;
    ToggleButton tBtnWed;
    ToggleButton tBtnThu;
    ToggleButton tBtnFri;
    ToggleButton tBtnSat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_alarm);

        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        tBtnSun = (ToggleButton) findViewById(R.id.toggle_sun);
        tBtnMon = (ToggleButton) findViewById(R.id.toggle_mon);
        tBtnTue = (ToggleButton) findViewById(R.id.toggle_tue);
        tBtnWed = (ToggleButton) findViewById(R.id.toggle_wed);
        tBtnThu = (ToggleButton) findViewById(R.id.toggle_thu);
        tBtnFri = (ToggleButton) findViewById(R.id.toggle_fri);
        tBtnSat = (ToggleButton) findViewById(R.id.toggle_sat);

        btnRegister = (Button) findViewById(R.id.button1);
        btnUnRegister = (Button) findViewById(R.id.button2);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, " register");

                // Calendar의 sunday=1 이라서 0의 자리에는 아무 값이나 넣었음
                boolean[] week = {false, tBtnSun.isChecked(), tBtnMon.isChecked(), tBtnTue.isChecked(), tBtnWed.isChecked(), tBtnThu.isChecked(), tBtnFri.isChecked(), tBtnSat.isChecked()};

                Intent intent = new Intent(WeekAlarmActivity.this, AlarmWeekReceiver.class);
                intent.putExtra("weekday", week);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(WeekAlarmActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar calendar = Calendar.getInstance();
                 calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + 10);  //10초 뒤

                long oneday = 24 * 60 * 60 * 1000;  //24시간

                // 10초 뒤에 시작해서 매일 같은 시간에 반복하기
                //(타입, 시작 시간, 주기, 수행할 작업을 가지는 인텐트)
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), oneday, pendingIntent);
            }
        });

        btnUnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, " unregister");

                Intent intent = new Intent(WeekAlarmActivity.this, AlarmWeekReceiver.class);

                // (Context, request code, intent, flag)
                PendingIntent pendingIntent = PendingIntent.getBroadcast(WeekAlarmActivity.this, 0, intent, 0);

                //request code가 같은 것을 취소
                am.cancel(pendingIntent);
            }
        });
    }
}
