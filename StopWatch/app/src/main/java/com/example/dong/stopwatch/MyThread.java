package com.example.dong.stopwatch;

import android.os.Handler;
import android.os.Message;

/**
 * Created by 102-9 on 2015-01-16.
 */
public class MyThread extends Thread {
    private int time = 0, sTime = 0, mTime = 0;  //0.1초, 초, 분
    private boolean isRunnable = false;
    Handler handler;

    MyThread(Handler handler) {
        this.handler = handler;
    }

    public void watchStart(){  //시작 메소드
        isRunnable = true;
    }

    public void watchStop(){  //일시정지 메소드
        isRunnable = false;
    }

    public void watchRestart(){  //재시작하기위해 쓰레드 종료
        time = 0;
        sTime = 0;
        mTime = 0;
        Message msg = new Message();

        msg.what = 3;
        msg.arg1 = mTime;
        handler.sendMessage(msg);
    }

    @Override
    public void run() {
        while (true) {
            while (isRunnable) {
                        Message msg = new Message();

                        try {
                            Thread.sleep(100);  //0.1초의 딜레이
                        } catch (InterruptedException e) {
                        }

                        msg.what = 1;
                        time++;  //시간증가
                        msg.arg1 = time;

                        if (time == 10) {  //1초
                            msg.what = 2;
                            time = 0;
                            msg.arg2 = time;
                            sTime++;
                            msg.arg1 = sTime;
                        }
                        if (sTime == 60) {  //1분
                            msg.what = 3;
                            sTime = 0;
                            time = 0;
                            msg.arg2 = sTime;
                            mTime++;
                    msg.arg1 = mTime;
                }
                handler.sendMessage(msg);
            }
        }
    }
}
