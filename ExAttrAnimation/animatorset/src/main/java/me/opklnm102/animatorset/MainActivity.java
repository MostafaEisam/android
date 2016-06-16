package me.opklnm102.animatorset;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

/**
 * AnimationSet
 * 여러개의 애니메이션을 모아 놓은 집합
 * 2가지 방식으로 구성
 *
 * 1. 메소드 사용
 *    void playSequentially() - 순서대로 실행
 *    void playTogether() - 동시에 실행
 *    애니메이션을 개별적으로 나열하거나 컬렉션으로 묶어서 인자로 전달
 *    동시, 순차만 가능할뿐 일부 순서를 섬세하게 통제 불가
 *
 * 2. 빌더를 먼저 생성한 후 빌더의 메소드로 순서를 정한다.
 *    각 애니메이션에 대해 개별적으로 순서 지정 -> 더 유연
 *    AnimatorSet.Builder play(Animator anim)
 *    인자로 전달한 애니메이션이 기준
 *    후에 before(), after(), with()로 애니메이션 지정
 *    play(a).before(b) - b 이전에 a, ab순
 *    play(a).after(b) - b 이후에 a, ba순
 *    play(a).with(b) - a와 b 동시
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    AnimView mAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout rootView = (LinearLayout) findViewById(R.id.linearLayout_root);
        mAnimView = new AnimView(this);
        rootView.addView(mAnimView);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mAnimView.startAnim(v.getId());
    }
}
