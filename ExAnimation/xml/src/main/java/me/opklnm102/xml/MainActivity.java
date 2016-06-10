package me.opklnm102.xml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mLinearLayout;
    Button btnScale, btnTrans, btnRotate, btnAlpha, btnSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        btnScale = (Button) findViewById(R.id.button_scale);
        btnTrans = (Button) findViewById(R.id.button_trans);
        btnRotate = (Button) findViewById(R.id.button_rotate);
        btnAlpha = (Button) findViewById(R.id.button_alpha);
        btnSet = (Button) findViewById(R.id.button_set);

        btnScale.setOnClickListener(this);
        btnTrans.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
        btnAlpha.setOnClickListener(this);
        btnSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Animation anim = null;

        switch (v.getId()) {
            case R.id.button_trans:
                anim = AnimationUtils.loadAnimation(this, R.anim.translate);  //애니메이션 리소스 로드
                break;
            case R.id.button_rotate:
                anim = AnimationUtils.loadAnimation(this, R.anim.rotate);
                break;
            case R.id.button_scale:
                anim = AnimationUtils.loadAnimation(this, R.anim.scale);
                break;
            case R.id.button_alpha:
                anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
                break;
            case R.id.button_set:
                anim = AnimationUtils.loadAnimation(this, R.anim.set);
                break;
        }
        mLinearLayout.startAnimation(anim);
    }
}
