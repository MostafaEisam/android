package me.opklnm102.exblockgame;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


/**
 * 도형을 그리려면 Canvas사용
 * 빠른 그리기 처리가 필요할 때는 원하는 타이밍에 그릴수 있는 TextureView를 상속
 * 도형의 서식을 변경하려면 Paint 사용
 */
public class MainActivity extends AppCompatActivity {

    private GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGameView = new GameView(this);


        setContentView(mGameView);



    }

    @Override
    protected void onResume() {
        super.onResume();
        mGameView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGameView.stop();
    }
}
