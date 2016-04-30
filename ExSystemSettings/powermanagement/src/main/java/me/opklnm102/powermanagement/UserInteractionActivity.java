package me.opklnm102.powermanagement;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

/*
  WakeLock 객체는 화면을 켜둘 수 있어 편리하지만 배터리 사용 측면에서는 부작용이 있다.
  화면이 켜저있어 방전당할 수 있다.

  사용자의 반응이 없으면 잠금을 해제하고 습립모드로 들어가는것이 안전
  어떻게 사용자가 반응하는지 알것인가 -> onKeyDown(), onTouchEvent()로?? NoNo.. 힘들다.

  1.5버전부터 추가된 API 사용
  void Activity.onUserInteraction()
  키입력, 터치입력, 트랙볼 이벤트가 발생할 때마다 호출. -> 단지 사용자가 기기를 조작하고 있는지만 알려줌

  void Activity.onUserLeaveHint()
  사용자에 의해 Activity가 백드라운드로 전환되기 직전에 onPause() 앞에 호출. Home키를 누르기 직전이도 호출
  Back키를 누르거나 전화 통화앱이 올라올 때, 타이머에의해 종료될때는 호출되지 않는다.
*/
public class UserInteractionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interaction);
    }

    /**
     * 이 예제는 너무 원론적
     * 메소드가 너무 많다.
     * 주기적으로 실행되는 핸들러가 이미 있다면 시간을 비교하는 방식이 더 간편
     */
    protected Handler mFinishHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            finish();  //메시지를 받자마자 Activity 종료
        }
    };

    void registerFinish(){  //5초후에 메시지를 보내 종료를 예약
        mFinishHandler.sendEmptyMessageDelayed(0, 5 * 1000);  //카메라의 경우 1분으로 설정
    }

    void unRegisterFinish(){  //큐에 쌓인 메시지를 제거하여 예약을 해제
        mFinishHandler.removeMessages(0);
    }

    void refreshFinish(){
        unRegisterFinish();
        registerFinish();
    }

    @Override
    protected void onResume() {  //종료 예약
        super.onResume();
        registerFinish();
    }

    @Override
    protected void onPause() {  //예약을 취소
        unRegisterFinish();
        super.onPause();
    }

    @Override
    public void onUserInteraction() {  //사용자의 입력이 있을 때 종료 예약 리셋
        super.onUserInteraction();
        refreshFinish();
    }

    @Override
    protected void onUserLeaveHint() {  //Home키를 눌러 Activity를 빠져나가면 호출. 백그라운드로 전환됬지만 살아있다.
        super.onUserLeaveHint();
        Toast.makeText(UserInteractionActivity.this, "Leave by user", Toast.LENGTH_SHORT).show();
    }
}
