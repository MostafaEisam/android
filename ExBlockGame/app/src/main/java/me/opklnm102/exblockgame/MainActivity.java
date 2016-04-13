package me.opklnm102.exblockgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;


/**
 * 도형을 그리려면 Canvas사용
 * 빠른 그리기 처리가 필요할 때는 원하는 타이밍에 그릴수 있는 TextureView를 상속
 * 도형의 서식을 변경하려면 Paint 사용
 */
public class MainActivity extends AppCompatActivity {

    private GameView mGameView;


    //화면이 회전하면 onCreate() 호출
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGameView = new GameView(this, savedInstanceState);

        //액션바에 오버플로 메뉴가 항상 표시되게 설정
        try {
            ViewConfiguration configuration = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(configuration, false);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mGameView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
