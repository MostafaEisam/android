package kr.co.mash_up.excanvas.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kr.co.mash_up.excanvas.view.MyViewDrawPath;

/*
    게임처럼 움직임이 복잡하고 변화가 많거나 디자인을 자유자재로 만들기 위해서는 직접 그려야 한다.
    캔버스에 직접 출력하려면 View를 상속받아 커스텀 뷰를 정의
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = new MyViewDrawPath(this);

        setContentView(view);
    }


}
