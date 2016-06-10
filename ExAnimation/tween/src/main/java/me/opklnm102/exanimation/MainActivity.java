package me.opklnm102.exanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Tween Animation
 * 대상의 초기상태와 마지막상태를 지정하여 수학적 계산에 의해 중간장면을 연속적으로 생성하는 방식
 * 위치이동이나 크기변경, 회전 등의 효과를 일정한 시간 내에 수행하여 애니메이션을 진행
 * 수학적 계산에 의해 장면을 생성하므로 움직임이 부드럽고 여러가지 효과를 조합할 수 있다.
 * 플래시의 애니메이션 방식과 유사
 * 적용대상에 따라 뷰, 레이아웃 애니메이션으로 세분화
 * ex) 초기좌표 0, 마지막좌표 100, 지속시간 10초면 1초에 10씩 증가하면서 뷰를 이동
 * 프레임을 교체하는 방식에 비해 연산이 필요하므로 CPU는 더 소모하지만
 * 중간 프레임을 생성하는 수식만 정의함으로써 용량은 훨씬 더 작다.
 * 중간단계의 프레임까지 섬세하게 생성해낼 수 있어 품질이 우수하며 여러개의 애니메이션을 조합하면 다양한 형태의 응용 가능
 * 프레임마다 좌표값, 크기, 회전, 투명도 등의 속성을 점진적으로 변경함으로써 수행
 * <p/>
 * Animation
 * | --- TranslateAnimation. 좌표값 변경
 * | --- ScaleAnimation. 크기 변경
 * | --- RotateAnimation. 회전각도 변경
 * | --- AlphaAnimation. 투명도 변경
 * | --- AnimationSet. 애니메이션의 조합
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv;
    Button btnTrans1, btnTrans2, btnTrans3;
    Button btnRotate1, btnRotate2, btnRotate3;
    Button btnScale1, btnScale2, btnScale3;
    Button btnAlpha1, btnAlpha2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.imageView);
        btnTrans1 = (Button) findViewById(R.id.button_trans1);
        btnTrans2 = (Button) findViewById(R.id.button_trans2);
        btnTrans3 = (Button) findViewById(R.id.button_trans3);
        btnRotate1 = (Button) findViewById(R.id.button_rotate1);
        btnRotate2 = (Button) findViewById(R.id.button_rotate2);
        btnRotate3 = (Button) findViewById(R.id.button_rotate3);
        btnScale1 = (Button) findViewById(R.id.button_scale1);
        btnScale2 = (Button) findViewById(R.id.button_scale2);
        btnScale3 = (Button) findViewById(R.id.button_scale3);
        btnAlpha1 = (Button) findViewById(R.id.button_alpha1);
        btnAlpha2 = (Button) findViewById(R.id.button_alpha2);

        btnTrans1.setOnClickListener(this);
        btnTrans2.setOnClickListener(this);
        btnTrans3.setOnClickListener(this);
        btnRotate1.setOnClickListener(this);
        btnRotate2.setOnClickListener(this);
        btnRotate3.setOnClickListener(this);
        btnScale1.setOnClickListener(this);
        btnScale2.setOnClickListener(this);
        btnScale3.setOnClickListener(this);
        btnAlpha1.setOnClickListener(this);
        btnAlpha2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Animation anim = null;

        switch (v.getId()) {
            case R.id.button_trans1:  //0~200 이동
                anim = new TranslateAnimation(0, 200, 0, 0);
                break;
            case R.id.button_trans2:  //자기폭 만큼 이동
                anim = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1,
                        Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
                break;
            case R.id.button_trans3:  //부모폭 만큼 이동
                anim = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 1,
                        Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
                break;
            case R.id.button_rotate1:  //최상단 기준 회전
                anim = new RotateAnimation(0, -180);
                break;
            case R.id.button_rotate2:  //중심 기준 회전
                anim = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                break;
            case R.id.button_rotate3:  //부모 우하단 기준 회전
                anim = new RotateAnimation(0, 90, Animation.RELATIVE_TO_PARENT, 0.5f,
                        Animation.RELATIVE_TO_PARENT, 1.0f);
                break;
            case R.id.button_scale1:  //최상단 확대
                anim = new ScaleAnimation(0, 1, 0, 1);
                break;
            case R.id.button_scale2:  //중심 확대
                anim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                break;
            case R.id.button_scale3:  //중심 축소
                anim = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                break;
            case R.id.button_alpha1:  //점점 밝게
                anim = new AlphaAnimation(0, 1);
                break;
            case R.id.button_alpha2:  //점점 흐리게
                anim = new AlphaAnimation(1, 0);
                break;
        }
        anim.setDuration(1000);
        iv.startAnimation(anim);
    }
}
