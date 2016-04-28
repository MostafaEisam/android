package me.opklnm102.excustomview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/*
   커스텀뷰 - 모양과 기능을 마음대로 디자인.
   임의의 위치에 원하는 그림을 그려 넣고 기능도 완전히 다시 정의

   만드는 방법
   1. 기존 위젯 클래스르 상속받아 기능을 확장하거나 수정
     전통적인 클래스 상속 기법이 적용되어 이해하기 쉽다.
     대부분의 기능을 슈퍼 클래스에서 빌려 쓰고 꼭 필요한 부분만 살짝 수정하는 방식이어서 만들기도 쉽다.

   2. 단순한 기능을 제공하는 기존 위젯을 결합하여 복잡한 동작을 수행하는 위젯 그룹을 정의
     ViewGroup이나 파생 클래스를 확장하여 만들며 그룹 내부 위젯끼리의 상호작용까지도 정의

   3. 기존에 없었던 완전히 새로운 위젯을 만든다.
      기능이나 모양이 너무 특수해서 기존 위젯을 수정하거나 결합하는 정도로는 어림없을 때 사용하는 방법
      최상위 위젯 클래스인 View를 상속받으며 맨땅에 헤딩이어서 난이도는 높지만 대신 자유도는 가장 우수
 */
public class MainActivity extends AppCompatActivity {

    RainbowProgress mRainbowProgress;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRainbowProgress = (RainbowProgress) findViewById(R.id.progress);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                if(mRainbowProgress.getPos() == 0){
                    mRainbowProgress.setPos(0);
                    mHandler.sendEmptyMessage(0);
                }
            }
        });

        mHandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                int pos;
                pos = mRainbowProgress.getPos();
                if(pos < mRainbowProgress.getMax()){
                    mRainbowProgress.setPos(pos + 1);
                    mHandler.sendEmptyMessageDelayed(0, 100);
                }else {
                    Toast.makeText(MainActivity.this, "Completed" ,Toast.LENGTH_SHORT).show();
                    mRainbowProgress.setPos(0);
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
