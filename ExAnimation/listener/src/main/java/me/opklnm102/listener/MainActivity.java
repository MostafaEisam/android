package me.opklnm102.listener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 애니메이션은 일단 시작만 시켜놓으면 내부적인 규칙에 따라 알아서 실행되고 끝나면 자동으로 정지
 * 종료후 바로 특정작업을 해야한다면 리스너 등록
 */
public class MainActivity extends AppCompatActivity {

    LinearLayout mLinearLayout;
    Button btnStart;
    Animation mAnimation1, mAnimation2, mAnimation3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        btnStart = (Button) findViewById(R.id.button_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                btnStart.startAnimation(mAnimation1);

                //애니메이션 연속 처리에는 오프셋을 사용하는 것이 정석
                btnStart.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.offset));
            }
        });

//        mAnimation1 = AnimationUtils.loadAnimation(this, R.anim.rotate);
//        mAnimation2 = AnimationUtils.loadAnimation(this, R.anim.alpha);
//        mAnimation3 = AnimationUtils.loadAnimation(this, R.anim.scale);
//
//        mAnimation1.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                btnStart.startAnimation(mAnimation2);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//
//        mAnimation2.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                btnStart.startAnimation(mAnimation3);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//
//        mAnimation3.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Toast.makeText(MainActivity.this, "Animation End", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
    }
}
