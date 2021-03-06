package com.example.dong.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView mV, sV, oHsV;
    Button sBtn, pBtn, rBtn;
    //int time = 0, sTime = 0, mTime = 0;
    String zero = "0";
    boolean threadStart = false;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 3) {  //분 set
                oHsV.setText("0");
                sV.setText("00");
                if (0 <= msg.arg1 && msg.arg1 <= 9)
                    mV.setText(zero + String.valueOf(msg.arg1));
                else
                    mV.setText(String.valueOf(msg.arg1));
            } else if (msg.what == 2) {  //초 set
                oHsV.setText("0");
                if (0 <= msg.arg1 && msg.arg1 <= 9)
                    sV.setText(zero + String.valueOf(msg.arg1));
                else
                    sV.setText(String.valueOf(msg.arg1));
            } else if (msg.what == 1) {  //0.1초 set
                oHsV.setText(String.valueOf(msg.arg1));
            }
        }
    };

    MyThread mThread = new MyThread(handler);  //쓰레드 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mV = (TextView) findViewById(R.id.minute);
        sV = (TextView) findViewById(R.id.sec);
        oHsV = (TextView) findViewById(R.id.oneHundredMsec);

        sBtn = (Button) findViewById(R.id.Start);
        pBtn = (Button) findViewById(R.id.Pause);
        rBtn = (Button) findViewById(R.id.Reset);
        sBtn.setOnClickListener(this);
        pBtn.setOnClickListener(this);
        rBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //무상태에서 스타트만 작동, 스타트에서 포즈 작동,
            // 포즈에서 스타트 리스타트 작동, 리스타트시 초기 상태로
            case R.id.Start:  //시작
                if (threadStart)
                    mThread.watchStart();
                else {
                    mThread.watchStart();
                    mThread.start();  //쓰레드 시작
                    threadStart = true;
                }
                sBtn.setEnabled(false);
                pBtn.setEnabled(true);
                rBtn.setEnabled(false);
                break;
            case R.id.Pause:  //일시정지
                mThread.watchStop();
                pBtn.setEnabled(false);
                sBtn.setEnabled(true);
                rBtn.setEnabled(true);
                break;
            case R.id.Reset:  //재시작
                mThread.watchRestart();
                sBtn.setEnabled(true);
                pBtn.setEnabled(false);
                rBtn.setEnabled(false);
                break;
        }
    }
}


