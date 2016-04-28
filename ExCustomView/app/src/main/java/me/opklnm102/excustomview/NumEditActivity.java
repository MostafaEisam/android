package me.opklnm102.excustomview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
  개별 위젯의 기능은 원자적이고 단순
  복잡한 작업을 수행하려면 여러위젯이 상호 협력적으로 동작
  위젯을 하나의 그룹으로 묶어 새로운 위젯으로 정의
  유기적으로 동작하는 위젯 그룹을 하나의 단위로 묶음으로써 통합성과 재사용성이 향상
 */
public class NumEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_edit);
    }
}
