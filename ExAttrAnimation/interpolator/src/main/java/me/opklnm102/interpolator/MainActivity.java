package me.opklnm102.interpolator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

/**
 * TimeInterpolator 인터페이스의
 * float getInterpolation(float input)을 재정의해 커스텀
 *
 * input인수로 0 ~ 1사이의 애니메이션 경과 시간을 전달하면 이떄 적용할 애니메이션 값을
 * 0 ~ 1사이의 실수로 리턴(0-시작, 1-끝)
 * 그대로 리턴하면 리니어 보간,
 * 함수를 적용하여 중간값을 만들어 내면 커스텀 보간
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    AnimView mAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout rootView = (LinearLayout) findViewById(R.id.linearLayout_root);
        mAnimView = new AnimView(this);
        rootView.addView(mAnimView);

        findViewById(R.id.button_linear).setOnClickListener(this);
        findViewById(R.id.button_accelerate).setOnClickListener(this);
        findViewById(R.id.button_decelerate).setOnClickListener(this);
        findViewById(R.id.button_bounce).setOnClickListener(this);
        findViewById(R.id.button_overshoot).setOnClickListener(this);
        findViewById(R.id.button_cycle).setOnClickListener(this);
        findViewById(R.id.button_anticipate).setOnClickListener(this);
        findViewById(R.id.button_reverse_linear).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_linear:
                mAnimView.startAnim(new LinearInterpolator());
                break;
            case R.id.button_accelerate:
                mAnimView.startAnim(new AccelerateInterpolator());
                break;
            case R.id.button_decelerate:
                mAnimView.startAnim(new DecelerateInterpolator());
                break;
            case R.id.button_bounce:
                mAnimView.startAnim(new BounceInterpolator());
                break;
            case R.id.button_overshoot:
                mAnimView.startAnim(new OvershootInterpolator());
                break;
            case R.id.button_cycle:
                mAnimView.startAnim(new CycleInterpolator(3f));
                break;
            case R.id.button_anticipate:
                mAnimView.startAnim(new AnticipateInterpolator());
                break;
            case R.id.button_reverse_linear:
                mAnimView.startAnim(new MyInterpolator());
                break;
        }
    }
}
