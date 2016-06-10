package me.opklnm102.attr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;

/**
 * Animation클래스는 자신이 조작하는 속성에 대한 시작값, 끝값, 중심점 정도의 필수 정보만 가지며
 * 애니메이션 자체의 속성은 직접적으로 가지지 않는다. Animation 추상클래스로 제공
 * 일부 속성은 단독 애니메이션에는 효과가 없다.
 * <p/>
 * duration - 애니메이션 실행시간을 1/1000초 단위로 지정(보통 0.1 ~ 0.5가 적당, 길어도 1초를 넘지않는것이 좋다)
 * fillAfter - 애니메이션 종료시 마지막 상태를 유지.(default=false)
 * fillBefore - 애니메이션 시작시 최초의 상태를 적용.(default=true)
 * fillEnabled - fillAfter, fillBefore 속성의 적용 여부
 * repeatCount - 반복회수를 지정, 0(반복X), 1(2회 반복), -1(무한 반복)
 * repeatMode - 반복할 떄의 동작지정. RESTART(다시 시작), REVERSE(반대로 진행)
 * startOffset - 애니메이션 시작전에 대기할 시간 지정, 복수 개의 애니메이션을 시차에 따라 진행할 때 사용
 * interpolator - 애니메이션 진행 속도의 변화 방식 지정(default=linear)
 * detachWallpaper - 벽지 위에서 애니메이션할 때 벽지에서 분리하여 벽지가 애니메이션되지 않도록 한다.
 * zAdjustment - 애니메이션중에 다른 뷰와 결칠때의 Z순서를 지정
 *               ZORDER_NORMAL: 원래값 유지
 *               ZORDER_TOP: 최상위로 올라옴
 *               ZORDER_BOTTOM: 최하위로 내려감
 *
 * fillAfter, fillBefore는 애니메이션 집합에서 애니메이션끼리 연결할 때 주로 사용
 * interpolator
 *     | --- linear_interpolator. 속도 일정
 *     | --- accelerate_interpolator. 처음에 느리다가 점점 빨라진다.
 *     | --- decelerate_interpolator. 처음에 빠르다가 점점 느려진다.
 *     | --- accelerate_decelerate_interpolator. 빨라지다가 다시 느려진다.
 *     | --- anticipate_interpolator. 역순 진행했다가 가속 진행
 *     | --- overshoot_interpolator. 끝부분에서 좀더 진행
 *     | --- anticipateovershoot_interpolator. 위 두 방식의 조합
 *     | --- bounce_interpolator. 끝부분에서 잔 진동을 보여준다.
 *     | --- cycle_interpolator. 끝까지 진행했다가 다시 복귀
 * 단순이동이면 linear가 무난, 퇴장이면 증가인터폴레이터, 등장이면 감소인터폴레이터가 어울린다.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView ivTarget;
    CheckBox cbBefore;
    CheckBox cbAfter;
    CheckBox cbRepeat;
    CheckBox cbReverse;
    RadioGroup rgInterpol;
    Spinner spinInterpol;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivTarget = (ImageView) findViewById(R.id.imageView_target);
        cbBefore = (CheckBox) findViewById(R.id.checkBox_fillbefore);
        cbAfter = (CheckBox) findViewById(R.id.checkBox_fillafter);
        cbRepeat = (CheckBox) findViewById(R.id.checkBox_repeat);
        cbReverse = (CheckBox) findViewById(R.id.checkBox_reverse);
        btnStart = (Button) findViewById(R.id.button_start);

        spinInterpol = (Spinner) findViewById(R.id.spinner_interpol);
        spinInterpol.setPrompt("Select Interpolator");

        ArrayAdapter<CharSequence> adspin = ArrayAdapter.createFromResource(
                this, R.array.interpolator, android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinInterpol.setAdapter(adspin);

        btnStart.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_start:
                TranslateAnimation trans = new TranslateAnimation(0, 250, 0, 0);
                trans.setDuration(2000);
                trans.setFillBefore(cbBefore.isChecked());
                trans.setFillAfter(cbAfter.isChecked());
                if(cbRepeat.isChecked()){
                    trans.setRepeatCount(1);
                    if(cbReverse.isChecked()){
                        trans.setRepeatMode(Animation.REVERSE);
                    }
                }
                switch (spinInterpol.getSelectedItemPosition()){
                    case 0:
                        trans.setInterpolator(new LinearInterpolator());
                        break;
                    case 1:
                        trans.setInterpolator(new AccelerateInterpolator());
                        break;
                    case 2:
                        trans.setInterpolator(new DecelerateInterpolator());
                        break;
                    case 3:
                        trans.setInterpolator(new AccelerateDecelerateInterpolator());
                        break;
                    case 4:
                        trans.setInterpolator(new AnticipateInterpolator());
                        break;
                    case 5:
                        trans.setInterpolator(new BounceInterpolator());
                        break;
                    case 6:
                        trans.setInterpolator(new CycleInterpolator(0.5f));
                        break;
                    case 7:
                        trans.setInterpolator(new OvershootInterpolator());
                        break;
                    case 8:
                        trans.setInterpolator(new AnticipateOvershootInterpolator());
                        break;
                }
                ivTarget.startAnimation(trans);
                break;
        }
    }
}
