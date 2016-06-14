package me.opklnm102.layout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 액티비티 애니메이션
 * 액티비티를 시작하거나 백그라운드로 전환될 때 OS에 의해 강제적으로 적용
 * 전역설정을 따르므로 모든 액티비티의 애니메이션이 같다.
 *
 * 커스텀하고 싶다면
 * void overridePendingTransition(int enterAnim, int exitAnim)
 * 를 실행 직후나 finish()로 종료 직전에 호출
 */
public class ActivityAnimActivity extends AppCompatActivity {

    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_anim);

        //애니메이션을 하지말란 뜻
        //호출한쪽에서 startActivity() 후에 애니메이션을 변경하는 것이 원칙
        overridePendingTransition(0, 0);

        btnExit = (Button) findViewById(R.id.button_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.anim_activity_exit);
            }
        });
    }
}
