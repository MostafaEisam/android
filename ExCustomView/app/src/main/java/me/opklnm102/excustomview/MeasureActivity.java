package me.opklnm102.excustomview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static me.opklnm102.excustomview.R.id.measView;

public class MeasureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        final MeasView mv = (MeasView) findViewById(measView);
        final TextView tv = (TextView) findViewById(R.id.textView);

        tv.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setText(mv.strResult);
            }
        }, 100);
    }
}
