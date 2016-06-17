package opklnm102.me.newswidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

/**
 * 설정 액티비티, 위젯을 설치할 때마다 관리자에 의해 자동 호출
 * 최초 설치할 때 외에는 설정 액티비티를 다시 볼 수 없다.
 * 변경하려면 제거하고 다시 설치
 *
 * 앱위젯이 사용할 설정값을 보여주고 편집한 후 영구적인 저장소에 저장
 *
 * 앱위젯 설치시 관리자와 설정 액티비티의 동작
 *
 * 설정 액티비타가 존재하는가?    --------- No ------------------> UPDATE 방송 . onUpdate() 호출
 *           YES                                                                         |
 *            |                                                                          |
 *    설정 액티비티 호출 ----------> 사용자가 설치 버튼을 눌렀는가?  --- YES ----        |
 *                                                |                             |        |
 *                                                No                            |        |
 *                                                |                             |        |
 *                                    DELETE 방송. onDelete() 호출           Update ~ 메소드
 *
 */
public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    final static String PREF = "NewWidget";
    CheckBox cbRed;
    int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //일단 실패로 가정
        setResult(RESULT_CANCELED);
        cbRed = (CheckBox) findViewById(R.id.checkBox_red);

        //ID 조사해 둔다.
        Intent intent = getIntent();
        mId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d(NewsWidget.TAG, "Config onCreate id = " + mId);

        //설정값 읽어서 체크박스에 출력
        //여러개의 인스턴스를 지원하기 위해 id 포함
        SharedPreferences prefs = getSharedPreferences(PREF, 0);
        boolean isRed = prefs.getBoolean("red_" + mId, false);
        cbRed.setChecked(isRed);

        findViewById(R.id.button_ok).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_ok:
                Log.d(NewsWidget.TAG, "Install id = " + mId);

                //인스턴스의 정보에 체크박스값 저장
                SharedPreferences prefs = getSharedPreferences(PREF, 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("red_" + mId, cbRed.isChecked());
                editor.commit();

                //상태갱신
                Context context = ConfigActivity.this;
                NewsWidget.updateNews(context, AppWidgetManager.getInstance(context), mId);

                //OK리턴 보냄
                Intent intent = new Intent();
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mId);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
