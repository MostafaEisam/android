package me.opklnm102.powermanagement;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserInteraction2Activity extends AppCompatActivity {

    long mLastInteraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interaction2);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //실행중에 1초에 1번씩 계속 호출 -> 타이머로 이용
    protected Handler mFinishHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            mFinishHandler.sendEmptyMessageDelayed(0, 1000);  //1초에 1번씩 호출할 수 있게 메시지를 큐에 넣는다.
            long now = System.currentTimeMillis();
            if((now - mLastInteraction) > 5000){
                finish();
            }
        }
    };

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        mLastInteraction = System.currentTimeMillis();
    }
}
