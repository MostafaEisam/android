package com.example.dong.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    TextView mV, sV, oHsV;
    Button sBtn, pBtn, rBtn;
    int time = 0, sTime = 0, mTime = 0;
    String zero = "0";

    Thread mThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while(Thread.currentThread() == mThread){
                Message msg = new Message();
                time++;
                if (time == 10) {
                    time = 0;
                    sTime++;
                }
                if (sTime == 60) {
                    sTime = 0;
                    mTime++;
                }
                //보낸다.
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    });

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (0 <= mTime && mTime <= 9)
                    mV.setText(zero + String.valueOf(mTime));
                else
                    mV.setText(String.valueOf(mTime));
            } else if (msg.what == 2) {
                if (0 <= mTime && mTime <= 9)
                    mV.setText(zero + String.valueOf(mTime));
                else
                    mV.setText(String.valueOf(mTime));
            } else if (msg.what == 3) {
                oHsV.setText(String.valueOf(time));
            }
        }
    };


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
    }
}