package me.opklnm102.powermanagement;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

/*
  화면 유지
  모바일은 배터리가 중요
  시스템은 일정시간이 지나면 화면, 키보드 조명, CPU를 잠재운다.
  그러나 무시해야하는 앱들이 존재.
  ex) 동영상 플레이어, 네비게이션, DMB, 카메라, mp3 플레이어(백그라운드 동작, CPU 잠들면 안됨) 등

  PowerManager를 이용
  화면의 켜짐을 조사하거나 즉시 화면을 끄거나 재부팅하는 등의 기능 제공
  PowerManager mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

  boolean isScreenOn()
  void goToSleep(long time) - 전원 버튼을 누르지 않아도 SW적으로 슬립 모드로 들어갈 수 있어서 실용성
                              아무나 사용X, 엄격한 제한(DEVICE_POWER 퍼미션, 시스템 앱으로 서명 필요)
  void reboot(String reason)
  void userActivity(long when, boolean noChangeLights)

  ** 유사한 개념의 WifiLock도 제공 - 대기모드에서 Wifi 제공

  PowerManager 자체는 전원관리 기능이 없으므로 WakeLock객체 이용
  WakeLock - 장비가 계속 깨어 있도록 잠그는 역할(화면, CPU가 켜진 상태를 유지하도록 강제함)
             화면의 가용성을 높여준다는 면에서 편리하지만 배터리 수명에는 치명적인 영향 -> 필요할 때만 가급적 짧게 사용
  PowerManager.WakeLock newWakeLock(int flags, String tag);  //(전원 관리 수준, 로그용 태그)

  * Permission 필요 -  <uses-permission android:name="android.permission.WAKE_LOCK"/>

   잠금 수준                   CPU        화면     키보드 조명
   PARTIAL_WAKE_LOCK      부분적 동작      끔          끔
   SCREEN_DIM_WAKE_LOCK      동작       흐리게 켬      끔
   SCREEN_BRIGHT_WAKE_LOCK   동작        밝게 켬       끔
   FULL_WAKE_LOCK            동작        밝게 켬       켬
   아래쪽으로 내려올수록 잠금의 강도가 높고 배터리 사용량 증가
   CPU만 계속 가동하고 싶다면 PARTIAL_WAKE_LOCK으로도 충분

   잠금 수준외에 화면관리 방식에 대한 2개의 옵션을 OR연산자로 지정가능(화면에 대한 옵션이므로 PARTIAL_WAKE_LOCK에는 효과없다)
   ACQUIRE_CAUSES_WAKEUP - 화면 잠금 기능은 켜진 화면을 계속 유지할뿐이지 꺼진 화면을 켜진않는다. 이 플래그를 지정하면
                           화면, 키보드조명을 강제로 켠다. 알람, 착신 통화처럼 사용자에게 즉시 통보하는 앱에 적당
   ON_AFTER_RELEASE - 화면 잠금이 끝난 후에 타이머를 리셋. 잠금이 끝난 후부터 설정의 화면 켜짐 시간만큼은 유지함으로써 켜진 시간을
                      더 연장하는 효과가 있다. 잠금과 풀림을 반복할 때 화면이 너무 빨리 꺼지지 않도록 한다.
*/
public class MainActivity extends AppCompatActivity {

    PowerManager mPowerManager;
    PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "WakeAlways");
        mWakeLock = mPowerManager.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, "WakeAlways");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // void acquire([long timeout])  객체의 설정 정보대로 전원이 관리. timeout(잠금을 자동으로 풀 시간을 지정)
        //생략시 별도의 지시가 있을 때까지 잠금이 유지
        mWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWakeLock.isHeld()) {  //현재 잠금상태인지 아닌지
            mWakeLock.release();  //잠금 해제
        }
    }
}
