
package me.opklnm102.layout;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * ViewSwitcher
 * 딱 2개의 child를 가지고 번갈아 교체하며 여러 child를 전환하는 효과를 낸다.
 * child는 레이아웃에 미리 추가해 놓거나 팩토리 인터페이스로 실행 중에 생성하는데
 * 보통 여러개의 내용물을 준비해 두고 View를 바꿔가며 보여주는 식이라 실행 중에 생성하는 것이 일반적
 *
 * TextSwitcher
 * TextView를 child로 가지며 2개의 child를 번갈아가며 보여준다.
 * 문자열 2개를 애니메이션으로 교체할 때 유용
 *
 * ImageSwitcher
 * 이미지를 여러개 준비해두고 교대로 보여줄 때 유용
 * 슬라이드쇼를 쉽게 구현할 수 있다.
 *
 */
public class TextSwitcherActivity extends AppCompatActivity {

    Button btnNext;
    TextSwitcher mTextSwitcher;
    int mAdIdx = 0;
    String[] arAd = new String[]{
            "aaaa",
            "bbbb",
            "cccc",
            "dddd"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_switcher);

        mTextSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
        mTextSwitcher.setFactory(mViewFactory);

        mTextSwitcher.setText(arAd[mAdIdx]);

        btnNext = (Button) findViewById(R.id.button_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdIdx = mAdIdx == arAd.length - 1 ? 0 : mAdIdx + 1;

                //새로운 문자열로 전환, Factory의 makeView() 호출
                //새로운 텍스트뷰를 생성하여 애니메이션 진행
                mTextSwitcher.setText(arAd[mAdIdx]);
            }
        });
    }

    ViewSwitcher.ViewFactory mViewFactory = new ViewSwitcher.ViewFactory() {
        @Override
        public View makeView() {
            TextView tv = new TextView(TextSwitcherActivity.this);
            tv.setTextSize(22);
            tv.setBackgroundColor(Color.YELLOW);
            tv.setTextColor(Color.BLACK);
            return tv;
        }
    };
}
