package me.dong.exwindowmanager.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import me.dong.exwindowmanager.R;

public class CallingService extends Service {

    public static final String TAG = CallingService.class.getSimpleName();
    public static final String EXTRA_CALL_NUMBER = "CALL_NUMBER";
    protected View rootView;

    TextView tvCallNumber;
    ImageButton ibClose;

    String callNumber;

    WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;

    public CallingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, " onCreate()");

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        Display display = mWindowManager.getDefaultDisplay();
        Point displayPoint = new Point();
        display.getSize(displayPoint);

        int width = (int) (displayPoint.x * 0.9);  //Display 가로 사이즈의 90%

        mParams = new WindowManager.LayoutParams(
                width,
                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,  //항상 최상위, 터치X
                WindowManager.LayoutParams.TYPE_PHONE,  //항상 최상위, 터치O
//                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  //포커스주지 않아 뷰 이외부분에 이벤트가 안먹는다.
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD  //키보드 안뜨게
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                PixelFormat.TRANSLUCENT);  //투명

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.call_popup_top, null);
        tvCallNumber = (TextView) rootView.findViewById(R.id.tv_call_number);
        ibClose = (ImageButton) rootView.findViewById(R.id.ib_close);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePopup();
            }
        });
        setDraggable();
    }


    /**
     * popup view를 이동시킬 수 있도록 설정
     */
    private void setDraggable() {
        rootView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = mParams.x;  //뷰 시작점
                        initialY = mParams.y;  //뷰 시작점
                        initialTouchX = event.getRawX();  //터치 시작점
                        initialTouchY = event.getRawY();  //터치 시작점
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //이동한거리 만큼 이동
                        mParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        mParams.y = initialY + (int) (event.getRawY() - initialTouchY);

                        if (rootView != null) {
                            mWindowManager.updateViewLayout(rootView, mParams);  //view update
                        }
                        return true;
                }
                return false;
            }
        });
    }

    @RequiresPermission(value = Manifest.permission.SYSTEM_ALERT_WINDOW)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, " onStartCommand()");

        mWindowManager.addView(rootView, mParams);  //최상위 윈도우에 뷰 넣기
        setExtra(intent);

        if (!TextUtils.isEmpty(callNumber)) {
            tvCallNumber.setText(callNumber);
        }

        /*
        메모리 공간 부족으로 서비스가 종료되었을 때,
        START_STICKY : 재생성과 onStartCommand() 호출(with null intent)
        START_NOT_STICKY : 서비스 재 실행하지 않음
        START_REDELIVER_INTENT : 재생성과 onStartCommand() 호출(with same intent)
         */
        return START_REDELIVER_INTENT;
    }

    private void setExtra(Intent intent) {
        if (intent == null) {
            removePopup();
            return;
        }
        callNumber = intent.getStringExtra(EXTRA_CALL_NUMBER);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removePopup();
    }

    public void removePopup() {
        if (rootView != null && mWindowManager != null) {
            mWindowManager.removeView(rootView);
//            mWindowManager.removeViewImmediate(rootView);
        }
    }
}
