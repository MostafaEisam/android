package me.opklnm102.frameanimation;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 애니메이션
 * 일정한 간격으로 출력물을 변경하여 움직이는 그림을 보여주는 그래픽 기법
 * 사용자의 시선을 끌고 프로그램의 품격을 높이는 훌륭한 수단
 * ex) 꿈틀대는 아이콘, 화면전환, 부드러운 스크롤
 *
 *
 * Frame Animation
 * 주기적으로 그림을 교체하는 전통적인 방법
 * 일련의 정지된 그림을 빠르게 교체하여 움직이는 것처럼 보인다.
 * 단순해서 사용하기 쉽지만 프레임이 많아지면 용량이 지나치게 늘어나 표현력의 한계가 있다.
 * 내부적으로 Ruannable객체를 만들어 주기적으로 그림을 교체
 * 그림만 잘 그리면 얼마든지 복잡한 애니메이션을 재생할 수 있어 자유도가 높지만
 * 프레임이 많고 주기가 짧을수록 용량이 급격하게 늘어나는 부담이 있다.
 */
public class MainActivity extends AppCompatActivity {

    AnimationDrawable mAnimationDrawable;
    ImageView iv;
    Button btnStart, btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.imageView);
        btnStart = (Button) findViewById(R.id.button_start);
        btnStop = (Button) findViewById(R.id.button_stop);

        //실행중에 setBackgroundResource()를 호출하여 다른 애니메이션으로 교체 가능
        mAnimationDrawable = (AnimationDrawable) iv.getBackground();

        iv.post(new Runnable() {
            @Override
            public void run() {
                mAnimationDrawable.start();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimationDrawable.start();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimationDrawable.stop();
            }
        });
    }
}
