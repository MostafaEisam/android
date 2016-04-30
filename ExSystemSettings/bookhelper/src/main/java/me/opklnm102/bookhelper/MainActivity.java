package me.opklnm102.bookhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
  책 읽는 것을 도와주는 유틸리티
  시작, 끝, 각페이지를 읽을 시간을 미리 입력해두면. 빨리 읽으라고 재촉
 */
public class MainActivity extends AppCompatActivity {

    EditText etStart, etEnd, etPeriod;
    CheckBox cbSound;
    TextView tvNowPage, tvRemain;
    Button btnStart, btnPause;

    int mNowPage, mRemain;
    SoundPool mSoundPool;
    int mSheet;
    PowerManager mPowerManager;
    PowerManager.WakeLock mWakeLock;
    public static final int IDLE = 0;
    public static final int COUNTING = 1;
    public static final int PAUSE = 2;
    int mStatus = IDLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();

        //프레퍼런스에서 정보 읽음
        SharedPreferences pref = getSharedPreferences("ReadingCounter", 0);
        int start = pref.getInt("start", 1);
        int end = pref.getInt("end", 5);
        int period = pref.getInt("period", 10);
        boolean sound = pref.getBoolean("sound", true);
        etStart.setText(Integer.toString(start));
        etEnd.setText(Integer.toString(end));
        etPeriod.setText(Integer.toString(period));
        cbSound.setChecked(sound);

        //책장 넘기는 소리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(6).build();
        } else {
            mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        mSheet = mSoundPool.load(this, R.raw.chicks, 1);

        // Button 핸들러 연결
        btnStart.setOnClickListener(mClickListener);
        btnPause.setOnClickListener(mClickListener);

        // WakeLock 객체 생성
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "tag");

    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }

        //프레퍼런스에 옵션값 기록
        try {
            SharedPreferences pref = getSharedPreferences("ReadingCounter", 0);
            SharedPreferences.Editor editor = pref.edit();
            int start, end, period;
            boolean sound;
            start = Integer.parseInt(etStart.getText().toString());
            end = Integer.parseInt(etEnd.getText().toString());
            period = Integer.parseInt(etPeriod.getText().toString());
            sound = cbSound.isChecked();
            editor.putInt("start", start);
            editor.putInt("end", end);
            editor.putInt("period", period);
            editor.putBoolean("sound", sound);
            editor.commit();
        } catch (NumberFormatException e) {
        }
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_start:
                    if (mStatus != IDLE) {
                        mTimerHandler.removeMessages(0);
                    }

                    //시작 페이지, 남은 시간만 읽고 타이머 바로 동작.
                    //끝, 시간, 사운드 여부는 중간에 바뀔 수도 있으므로 지금 조사하지 않음
                    int endPage;
                    try {
                        mNowPage = Integer.parseInt(etStart.getText().toString());
                        endPage = Integer.parseInt(etEnd.getText().toString());
                        mRemain = Integer.parseInt(etPeriod.getText().toString()) - 1;
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "독서 범위나 시간이 잘못 입력", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (mNowPage >= endPage) {
                        Toast.makeText(MainActivity.this, "끝 페이지가 시작 페이지보다 더 뒤쪽이어야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    tvNowPage.setText(Integer.toString(mNowPage));
                    tvRemain.setText(Integer.toString(mRemain));

                    mStatus = COUNTING;
                    btnPause.setText("잠시 중지");
                    mTimerHandler.sendEmptyMessageDelayed(0, 1000);
                    break;
                case R.id.button_pause:
                    if(mStatus == IDLE){
                        return;
                    }

                    if (mStatus == PAUSE){
                        mStatus = COUNTING;
                        btnPause.setText("잠시 중지");
                        mTimerHandler.sendEmptyMessageDelayed(0, 1000);
                        btnPause.setText("계속");
                    }
                    break;
            }
        }
    };

    Handler mTimerHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if(mStatus == PAUSE){
                return;
            }

            if(mRemain != 0){
                mRemain--;
                tvRemain.setText(Integer.toString(mRemain));
                mTimerHandler.sendEmptyMessageDelayed(0, 1000);
            }else {
                //소리부터 낸다.
                if (cbSound.isChecked()){
                    mSoundPool.play(mSheet, 1, 1, 0, 0, 1);
                }

                //끝 페이지를 지워 버렸으면 즉시 독서를 종료
                int endPage;
                try{
                    endPage = Integer.parseInt(etEnd.getText().toString());
                }catch (NumberFormatException e){
                    endPage = -1;
                }

                if(mNowPage < endPage){
                    mNowPage++;
                    tvNowPage.setText(Integer.toString(mNowPage));

                    //시간을 지워 버렸으면 60초로 디폴트 처리한다.
                    try{
                        mRemain = Integer.parseInt(etPeriod.getText().toString()) -1;
                    }catch (NumberFormatException e){
                        mRemain = 60;
                    }

                    tvRemain.setText(Integer.toString(mRemain));
                    mTimerHandler.sendEmptyMessageDelayed(0, 1000);
                }else {
                    Toast.makeText(MainActivity.this, "독서가 끝났습니다.", Toast.LENGTH_SHORT).show();
                    mStatus = IDLE;
                }
            }
        }
    };

    private void initView() {
        etStart = (EditText) findViewById(R.id.editText_start);
        etEnd = (EditText) findViewById(R.id.editText_end);
        etPeriod = (EditText) findViewById(R.id.editText_period);
        cbSound = (CheckBox) findViewById(R.id.checkbox_sound);
        tvNowPage = (TextView) findViewById(R.id.textView_now_page);
        tvRemain = (TextView) findViewById(R.id.textView_remain);
        btnStart = (Button) findViewById(R.id.button_start);
        btnPause = (Button) findViewById(R.id.button_pause);
    }
}

