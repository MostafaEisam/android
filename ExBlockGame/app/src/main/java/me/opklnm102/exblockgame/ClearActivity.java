package me.opklnm102.exblockgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;

public class ClearActivity extends AppCompatActivity {

    public static final String EXTRA_IS_CLEAR = "isClear";
    public static final String EXTRA_BLOCK_COUNT = "blockCount";
    public static final String EXTRA_TIME = "time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_clear);

        Intent receiveIntent = getIntent();

        if (receiveIntent == null) {
            finish();
        }

        Bundle receiveExtras = receiveIntent.getExtras();
        if (receiveExtras == null) {
            finish();
        }

        //넘어온 정보 가져오기
        boolean isClear = receiveExtras.getBoolean(EXTRA_IS_CLEAR, false);
        int blockCount = receiveExtras.getInt(EXTRA_BLOCK_COUNT, 0);
        long clearTime = receiveExtras.getLong(EXTRA_TIME, 0);

        TextView tvTitle = (TextView) findViewById(R.id.textTitle);
        TextView tvBlockCount = (TextView) findViewById(R.id.textBlockCount);
        TextView tvClearTime = (TextView) findViewById(R.id.textClearTime);
        Button btnGameStart = (Button) findViewById(R.id.buttonGameStart);

        //게임 오버, 클리어했을 때 표시할 문구 변경
        if (isClear) {
            tvTitle.setText(R.string.clear);
        } else {
            tvTitle.setText(R.string.game_over);
        }

        //남은 블록개수 표시
        tvBlockCount.setText(getString(R.string.block_count, blockCount));

        //클리어, 게임오버시 경과시간 표시
        tvClearTime.setText(getString(R.string.time, clearTime / 1000, clearTime % 1000));

        btnGameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClearActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);  //액티비티 히스토리에 기록되지 않아서 이화면으로 돌아갈수 없다.
                startActivity(intent);
            }
        });

        //Score 계산 처리
        TextView tvScore = (TextView) findViewById(R.id.textScore);
        final long score = (GameView.BLOCK_COUNT - blockCount) * clearTime;
        tvScore.setText(getString(R.string.score, score));

        //하이스코어 가저오기
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long highScore = sharedPreferences.getLong("high_score", 0);

        // High Score 갱신
        if (highScore < score) {
            highScore = score;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("high_score", highScore);
            editor.commit();  //아토믹을 지키기 위해 commit()을 호출해야만 저장
        }

        TextView tvHighScore = (TextView) findViewById(R.id.textHighScore);
        tvHighScore.setText(getString(R.string.high_score, highScore));

        Button btnShare = (Button) findViewById(R.id.buttonShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //암시적 인텐트
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");  //공유할 정보의 종류 지정
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.score, score));  //공유 정보 설정
                startActivity(intent);
            }
        });
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
