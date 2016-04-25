package kr.co.mash_up.excanvas.activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kr.co.mash_up.excanvas.R;

/*
  assets폴더에 있는 원하는 폰트를 앱에서 사용할 수 있다.
  시스템 글꼴이 아닌 App에서 내장할 수 있어 환경에 실행가능하다는 장점
 */
public class CustomFontActivity extends AppCompatActivity {

    Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_font);

        mTypeface = Typeface.createFromAsset(getAssets(), "bmdohyeon_ttf.ttf");
    }
}
