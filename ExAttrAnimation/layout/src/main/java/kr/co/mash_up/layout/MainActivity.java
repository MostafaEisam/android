package kr.co.mash_up.layout;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 여러 Child를 거느리는 뷰그룹은 Child를 추가, 제거 가능
 * Child 목록에 변화가 생기면(보이기 포함) 기존 Child의 위치나 크기가 재조정
 * 이때 새로 생성되는 Child가 갑자기 나타나는 것보다는 애니메이션되면서 나타나는게 보기에 좋다.
 *
 * LayoutTransition 객체를 생성하고 setAnimator()로 각 상황에 대한 애니메이션을 정의하고
 * setLayoutTransition()으로 뷰그룹에 등록해 놓으면 변화가 있을 때 수행된다.
 * ex) 버튼이 한바퀴 돌면서 등장, 사라지는 버튼이 왼쪽으로 휙 밀려서 떨어지는 효과 등. apiDemos 참고
 *
 * 지정 가능한 애니메이션 종류
 * APPEARING - 항목이 나타날 때
 * CHANGE_APPEARING - 새 항목이 추가되어 변화가 생겼을 때
 * DISAPPEARING - 항목이 사라질 때
 * CHANGE_DISAPPEARING - 기존 항목이 제거되어 변화가 생겼을 때
 * CHANGING - 추가나 삭제 외의 레이아웃 변화가 발생했을 때
 *
 * 각각의 변화에 대해 고유한 애니메이션 등록 가능
 * null로 지정시 애니메이션 수행X
 *
 * animateLayoutChanges를 true로 하면 default 애니메이션 사용 가능
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout mLinearLayout;
    int mCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_root);

        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button btn = new Button(this);
        btn.setText("B" + mCount);
        mCount++;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayout.removeView(v);
            }
        });
        mLinearLayout.addView(btn);
    }
}
