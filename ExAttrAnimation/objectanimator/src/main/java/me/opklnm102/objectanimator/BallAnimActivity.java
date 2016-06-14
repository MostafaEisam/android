package me.opklnm102.objectanimator;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 레이아웃 안에서 객체의 속성을 변경하는데는 제약이 따른다.
 * child의 크기 변경으로 다른 child의 크기가 변경될 수도 있기 때문에
 * 특정 속성 변경이 허락되지 않을 때도 있어 자유로운 애니메이션이 가능하려면 캔버스에 직접 그리는 것이 무난
 */
public class BallAnimActivity extends AppCompatActivity {

    Button btnRun;
    AnimView mAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball_anim);

        LinearLayout rootView = (LinearLayout) findViewById(R.id.linearLayout_root);
        mAnimView = new AnimView(this);
        rootView.addView(mAnimView);

        btnRun = (Button) findViewById(R.id.button_run);
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimView.startAnim();
            }
        });
    }
}
