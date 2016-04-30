package me.opklnm102.systemsetting;

import android.content.ContentResolver;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SetSettingActivity extends AppCompatActivity {

    ContentResolver mContentResolver;
    TextView tvTimeout;
    SeekBar sbTimeout;
    ToggleButton tbScreenRotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_setting);

        mContentResolver = getContentResolver();
        tvTimeout = (TextView) findViewById(R.id.textView_timeout);
        sbTimeout = (SeekBar) findViewById(R.id.seekBar_screen_timeout);
        tbScreenRotation = (ToggleButton) findViewById(R.id.toggleButton_autorotate);

        int timeout = Settings.System.getInt(mContentResolver, Settings.System.SCREEN_OFF_TIMEOUT, -1);
        sbTimeout.setProgress(timeout == -1 ? 0 : timeout / 10000);  //조명시간 단위는 1/1000이되 seekBar의 눈금하나는 10초이므로 10000으로 나누어 사용
        printTimeout(timeout);

        sbTimeout.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int timeout = progress == 0 ? -1 : progress * 10000;
                Settings.System.putInt(mContentResolver, Settings.System.SCREEN_OFF_TIMEOUT, timeout);
                printTimeout(timeout);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        int autorotate = Settings.System.getInt(mContentResolver, Settings.System.ACCELEROMETER_ROTATION, -1);
        tbScreenRotation.setChecked(autorotate == 1);

        tbScreenRotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tbScreenRotation.isChecked()) {
                    Settings.System.putInt(mContentResolver, Settings.System.ACCELEROMETER_ROTATION, 1);
                } else {
                    Settings.System.putInt(mContentResolver, Settings.System.ACCELEROMETER_ROTATION, 0);
                }
            }
        });
    }

    void printTimeout(int timeout) {
        if (timeout == -1) {
            tvTimeout.setText("조명 시간: (끄지 않음)");
        } else {
            tvTimeout.setText("조명 시간: " + (timeout / 1000) + "초");
        }
    }
}
