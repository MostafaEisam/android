package com.example.dong.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SecondActivity extends AppCompatActivity {
    TextView mV, sV, oHsV;
    Button sBtn, pBtn, rBtn;
    int time = 0, sTime = 0, mTime = 0;

    boolean mFlag = false, sFlag = false;
    boolean runFlag = false;

    Handler mHandler;
    Thread mThread;

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

        pBtn.setEnabled(false);
        rBtn.setEnabled(false);

        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runFlag = true;
                sBtn.setEnabled(false);
                pBtn.setEnabled(true);
            }
        });

        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runFlag = false;
                sBtn.setEnabled(true);
                pBtn.setEnabled(false);
                rBtn.setEnabled(true);
            }
        });

        rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sBtn.setEnabled(true);
                pBtn.setEnabled(false);
                rBtn.setEnabled(false);
                time = 0;
                sTime = 0;
                mTime = 0;
                mV.setText("00");
                sV.setText("00");
                oHsV.setText("0");
            }
        });

        mHandler = new Handler();
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (Thread.currentThread() == mThread) {
                    while (runFlag) {
                        time++;
                        if (time == 10) {
                            sFlag = true;
                            time = 0;
                            sTime++;
                        }
                        if (sTime == 60) {
                            mFlag = true;
                            time = 0;
                            sTime = 0;
                            mTime++;
                        }

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mFlag) {
                                    if (mTime < 10)
                                        mV.setText("0" + Integer.toString(mTime));
                                    else
                                        mV.setText(Integer.toString(mTime));
                                    mFlag = false;
                                }
                                if (sFlag) {
                                    if (sTime < 10)
                                        sV.setText("0" + Integer.toString(sTime));
                                    else
                                        sV.setText(Integer.toString(sTime));
                                    sFlag = false;
                                }
                                oHsV.setText(Integer.toString(time));
                            }
                        });
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        });
        mThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
