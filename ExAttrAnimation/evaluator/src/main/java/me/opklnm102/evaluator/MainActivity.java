package me.opklnm102.evaluator;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 평가자(Evaluator)
 * 애니메이션 중간값을 생성하는 역할
 * 시작, 끝, 진행 시간을 종합적으로 고려하여 속성에 대입할 값 결정
 * ex) 200에서 400까지 이동, 현재 진행시간이 0.5라면
 *     200 + (400-200) x 0.5 = 300을 생성
 *
 * 정수, 실수, 색상도 아닌 특별한 타입의 경우
 * TypeEvaluator<T>를 상속받아 T evaluate(float fraction, T startValue, T endValue)를 구현
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);

        findViewById(R.id.button_int_evaluator).setOnClickListener(this);
        findViewById(R.id.button_argb_evaluator).setOnClickListener(this);
    }

    //평가자에 따라 어떻게 다른지 알아보자.
    @Override
    public void onClick(View v) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(btn, "backgroundColor", Color.YELLOW, Color.RED);
        objectAnimator.setDuration(2000);
        if(v.getId() == R.id.button_argb_evaluator){
            objectAnimator.setEvaluator(new ArgbEvaluator());
        }
        objectAnimator.start();
    }
}
