package me.opklnm102.xml;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


/**
 * 속성 애니메이션도 XML 리소스로 정의해놓고 재사용할 수 있다.
 * res/animator에 놓자
 *
 * static Animator AnimatorInflater.loadAnimator(Context context, int id)로 리소스를 로드
 * 이후 필요한 추가 속성을 지정하고 start()로 실행
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

        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mAnimView.startAnim();
    }
}
