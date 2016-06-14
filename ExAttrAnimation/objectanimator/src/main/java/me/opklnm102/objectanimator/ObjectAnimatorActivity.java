package me.opklnm102.objectanimator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * ObjectAnimator
 * 적용 대상 객체와 속성을 지정하면 중간값을 대입하는 처리까지 자동으로 수행하는 고수준 클래스
 * 별도의 리스너 필요 X
 * 코드가 짧고 간편
 * ValueAnimator와 리스너의 속성 대입문을 합쳐 놓은 형태
 * 사용하기는 편리하지만 변경 가능한 속성의 종류는 제한적
 * setter, getter가 있는 속성에만 적용 가능
 *
 */
public class ObjectAnimatorActivity extends AppCompatActivity {

    Button btnRun;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        btnRun = (Button) findViewById(R.id.button_run);
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(btn, "width", 200, 400);
                objectAnimator.setInterpolator(null);
                objectAnimator.setRepeatCount(1);
                objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
                objectAnimator.setDuration(2000);
                objectAnimator.start();
            }
        });
    }
}
