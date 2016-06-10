package me.opklnm102.set;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 단일 애니메이션은 동작이 단순해서 다양한 효과를 내기 어렵지만 둘이상의 조합을 통해 활용성 증가
 * AnimationSet(boolean shareInterpolator) - 인터폴레이터를 공유할지 여부. 공유할 경우 집합의 인터폴레이터가 우선 적용
 * void addAnimation(Animation a)
 * <p/>
 * 얼마든지 많은 애니메이션을 동시에 실행할 수 있으며 그럼에도 불구하고 속도의 감소는 거의 없다.
 * 동시에 다수개의 애니메이션을 부드럽게 적용할 수 있는 이유는 변환 행렬을 사용하기 때문
 * 행렬은 하나의 객체로 여러개의 다항식을 만들어내므로 좌표와 크기, 회전각도를 원하는대로 조작,
 * 단순 행렬곱셈연산만으로 여러 변환을 누적 적용할 수 있다는 점에서 우수
 * <p/>
 * Custom Animation
 * 다음 메소드를 오버라이딩
 * 1. void initialize(int width, int height, int parentWidth, int parentHeight)
 * 애니메이션 실행 직전에 호출
 * 지속시간, 인터폴레이터 등을 초기화
 * 인자들은 중심점 계산시 이용
 * 2. void applyTransformation(float interpolatedTime, Transformation t)
 * 인자로 현재시간(0 ~ 1, 0-시작, 1-끝), 변환객체
 * 여기서 변환객체를 조작하여 원하는 효과를 만들어낸다.
 *
 * 트윈 애니메이션의 다양성은 무궁무진
 * 행렬 자체의 변환능력이 거의 무한대 + 변환을 조합 + 카메라같은 입체적인 변환기법까지 적용
 *
 * 좋은 애니메이션을 만들어내는데는 기술적 지식보다 예술적 자질이 필요
 */
public class MainActivity extends AppCompatActivity {

    ImageView ivTarget;
    Button btnStart;
    Button btnCamera;
    Button btnSkew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivTarget = (ImageView) findViewById(R.id.imageView_target);
        btnStart = (Button) findViewById(R.id.button_start);
        btnCamera = (Button) findViewById(R.id.button_camera);
        btnSkew = (Button) findViewById(R.id.button_skew);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.setInterpolator(new LinearInterpolator());

                TranslateAnimation trans = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 1,
                        Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
                trans.setDuration(3000);
                animationSet.addAnimation(trans);

                AlphaAnimation alpha = new AlphaAnimation(1, 0);
                alpha.setDuration(300);
                alpha.setStartOffset(500);
                alpha.setRepeatCount(4);
                alpha.setRepeatMode(Animation.REVERSE);
                animationSet.addAnimation(alpha);

                ivTarget.startAnimation(animationSet);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivTarget.startAnimation(new CameraAnim());
            }
        });

        btnSkew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivTarget.startAnimation(new SkewAnim());
            }
        });
    }

    class SkewAnim extends Animation {

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(1000);
            setInterpolator(new LinearInterpolator());
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            Matrix matrix = t.getMatrix();
            matrix.setSkew(0.5f * interpolatedTime, 0);
        }
    }

    class CameraAnim extends Animation {

        float cx, cy;  //중심 좌표

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            cx = width / 2;
            cy = height / 2;
            setDuration(1000);
            setInterpolator(new LinearInterpolator());
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            Camera cam = new Camera();
            cam.rotateY(360 * interpolatedTime);  //Y축 360도 회전
            Matrix matrix = t.getMatrix();
            cam.getMatrix(matrix);

            //회전 중심을 이미지 중심으로 하기 위해 카메라를 회전하기 전에 중심을 원점으로
            matrix.preTranslate(-cx, -cy);
            matrix.postTranslate(cx, cy);  //회전 후 원래 위치로
        }
    }
}
