package me.opklnm102.exattranimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 속성 애니메이션
 * 기존의 애니메이션은 움직임에 대한 정보만 가지며 대상이 애니메이션을 직접 실행하는 방식
 * 시작, 끝, 지속시간, 인터폴레이터 등의 움직임 관련정보를 애니메이션 객체에 설정하고 뷰에 대해 실행하면
 * 뷰가 애니메이션의 속성을 참조하여 스스로 움직인다.
 * 간편하지만 애니메이션 클래스가 제공하는 기능만 사용할 수 있으며 그 이상의 효과를 발휘하기 어렵다는 제약
 *
 * api 3.0부터 추가됨
 * 특정 속성값을 주어진 시간동안 변화를 주어 애니메이션하는 방식
 * 대상 속성에 제약이 없어 활용 범위가 넓다.
 * 3D효과, 화면에 나타나지 않는 속성값을 조작하여 간접적으로 애니메이션, 커스텀 속성까지 애니메이션 가능
 * 기존 애니메이션과 방식이 다를뿐 움직임을 구현한다는 목적이 같고 원리나 개념도 비슷
 * Animator ---- AnimatorSet
 *           |-- ValueAnimatorSet --- ObjectAnimator
 *
 * Animator
 * 추상 클래스
 * 애니메이션을 관리하는 기본 메소드 제공
 * 직접 사용X
 * void start() - 애니메이션을 처음부터 재생
 * void reverse() - 끝에서부터 역으로 재생
 * void end() - 마지막 상태로 이동한 후 정지
 * void cancel() - 현재 진행 상태에서 정지
 *
 * ValueAnimator
 * 시간의 흐름에 따라 중간값을 계산하여 주기적으로 알려주는 일종의 타이머
 * 시작, 끝, 지속시간, 인터폴레이터 등을 설정해 놓으면 시간 흐름과 보간 함수를 참조하여
 * 중간값을 계산하고 콜백으로 전달
 * 중간값을 계산할 뿐 어디에 사용하는지는 관여X -> 자유도가 높다.
 * 매번 중간값을 수신하여 원하는 속성값을 직접 조작 -> 불편
 */
public class ValueAnimatorActivity extends AppCompatActivity {

    Button btnRun;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valueanimator);

        btn = (Button) findViewById(R.id.button);
        btnRun = (Button) findViewById(R.id.button_run);
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //속성값이 2초동안 150에서 400까지 증가
                ValueAnimator valueAnimator = ValueAnimator.ofInt(150, 400);
                valueAnimator.setDuration(2000);
                valueAnimator.setInterpolator(null);
                valueAnimator.setRepeatCount(1);
                valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Toast.makeText(ValueAnimatorActivity.this, "end", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        Toast.makeText(ValueAnimatorActivity.this, "start", Toast.LENGTH_SHORT).show();
                    }
                });
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    //중간값을 애니메이션에 적용하기 위한 콜백
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        /*
                        setWidth()
                        버튼의 폭을 변경할 때마다 화면을 무효화하여 다시 그린다.
                        포함객체의 속성, 간접적으로 출력에 영향을 주는 속성, 커스텀 위젯의 경우
                        invalidate()를 직접 호출
                        화면을 다시 그리는 비율의 디폴트는 0.01초.
                        초당 100회 화면을 갱신 - 사람눈에 자연스럽게 인식
                         */
                        btn.setWidth((Integer)animation.getAnimatedValue());
                    }
                });
                valueAnimator.start();
            }
        });
    }
}
