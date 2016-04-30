package me.opklnm102.systemsetting;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
  설정
  시스템의 동작 방식이나 모양을 사용자의 취향에 맞게 조정하는 기능
  Android에서는 시스템의 모든 설정을 통합관리할 수 있는 환경설정(Settings) App 기본 제공

  설정을 변경하는 것은 폰 소유자의 고유 권한 -> 사용자가 조정하는것이 바람직
  앱은 사용자의 선택을 적용하기 위해 설정을 읽기만 한다.
  그러나 명시적인 허락이나 암묵적인 동의가 있다면 변경하는 것도 가능
  설정 정보는 시스템 DB에 저장
  종류에 따라 android.provider 패키지의 Settings에 몇개의 하위 클래스로 나누어져 있다.

  기본적인 설정은 Settings.System클래스가 정의
  SCREEN_OFF_TIMEOUT - 화면을 끌시간(단위 1/1000초). -1이면 끄지 않음
  SCREEN_BRIGHTNESS - 화면 밝기(0~255)
  SCREEN_BRIGHTNESS_MODE - 자동 화면밝기 모드 여부
                           SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                           SCREEN_BRIGHTNESS_MODE_MANUAL
  ACCELEROMETER_ROTATION - 가속 센서로 화면 회전 여부
  AIRPLANE_MODE_ON - 비행 모드 설정
  HAPTIC_FEEDBACK_ENABLED - 롱 프레스 등에 진동 기능을 쓸 것인지의 여부
  SOUND_EFFECTS_ENABLED - 버튼 클릭등에 사운드 효과를 쓸 것이지의 여부
  STAY_ON_WHILE_PLUGGED_IN - USB연결시 화면을 계속 유지할 것인지

  보안과 관련된 설정은 Settings.Secure클래스에 정의
  개인정보, 과금에 관련된 설정이라 읽을수만 있으며, 복잡한 절차를 걸쳐 특수한 API를 사용하여 수정
  BLUETOOTH_ON - 블루투스 장비 사용
  INSTALL_NON_MARKET_APPS - 마켓에서 다운받지 않은 앱 설치
  DATA_ROAMING - 데이터 로밍 사용 여부
  WIFI_ON - 와이파이 사용

 */
public class MainActivity extends AppCompatActivity {

    TextView tvResult;
    Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tvResult = (TextView) findViewById(R.id.textView_result);
        btnRefresh = (Button) findViewById(R.id.button_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSetting();
            }
        });

        refreshSetting();
    }

    void refreshSetting() {
        ContentResolver cr = getContentResolver();
        String result = "";
        result = String.format("화면 타임아웃 = %d\n화면 밝기 = %d\n자동 회전 = %d\n비행모드 = %d\n햅틱 = %d\n사운드 효과 = %d\n",
                Settings.System.getInt(cr, Settings.System.SCREEN_OFF_TIMEOUT, -1),
                Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS, -1),
                Settings.System.getInt(cr, Settings.System.ACCELEROMETER_ROTATION, -1),
                Settings.System.getInt(cr, Settings.Global.AIRPLANE_MODE_ON, -1),
                Settings.System.getInt(cr, Settings.System.HAPTIC_FEEDBACK_ENABLED, -1),
                Settings.System.getInt(cr, Settings.System.SOUND_EFFECTS_ENABLED, -1));
        tvResult.setText(result);
    }
}
