package me.opklnm102.layout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

/**
 * ViewAnimator
 * FrameLayout를 상속
 * 여러뷰를 겹쳐놓고 하나만 선택적으로 보여주되 자체적으로 애니메이션 제공
 * 실행중에 child 추가, 삭제 가능
 *
 * ViewFlipper
 * 일정간격으로 child를 교체하는 기능을 추가로 제공
 * flipInterval - 간격 지정
 * 프리젠테이션처럼 일련의 child를 교체하며 보여줄 때 유용
 */
public class ViewFliperActivity extends AppCompatActivity implements View.OnClickListener {

    ViewFlipper mViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fliper);

        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        findViewById(R.id.button_prev).setOnClickListener(this);
        findViewById(R.id.button_next).setOnClickListener(this);
        findViewById(R.id.checkBox_flip).setOnClickListener(this);
        findViewById(R.id.checkBox_anim).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_prev:
                mViewFlipper.showPrevious();
                break;
            case R.id.button_next:
                mViewFlipper.showNext();
                break;
            case R.id.checkBox_flip:
                if(mViewFlipper.isFlipping()){
                    mViewFlipper.stopFlipping();
                }else {
                    mViewFlipper.startFlipping();
                }
                break;
            case R.id.checkBox_anim:
                if(mViewFlipper.getInAnimation() == null){
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.view_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.view_out));
                }else{
                    mViewFlipper.setInAnimation(null);
                    mViewFlipper.setOutAnimation(null);
                }
                break;
        }
    }
}
