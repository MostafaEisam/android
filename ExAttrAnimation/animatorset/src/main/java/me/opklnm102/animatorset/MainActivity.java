package me.opklnm102.animatorset;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    AnimView mAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout rootView = (LinearLayout) findViewById(R.id.linearLayout_root);
        mAnimView = new AnimView(this);
        rootView.addView(mAnimView);
    }

    @Override
    public void onClick(View v) {
        mAnimView.startAnim(v.getId());
    }
}
