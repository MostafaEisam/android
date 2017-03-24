package me.dong.exviewoverlay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewOverlay;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // button with banner
        final Button btn = (Button) findViewById(R.id.button);
        final ViewOverlay viewOverlay = btn.getOverlay();
        final BannerDrawable bannerDrawable = new BannerDrawable();
        btn.post(new Runnable() {
            @Override
            public void run() {
                // top rigth square
                bannerDrawable.setBounds(btn.getWidth() / 2, 0, btn.getWidth(), btn.getHeight() / 2);
                viewOverlay.add(bannerDrawable);
            }
        });

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.content);
        for (int i = 0; i < 10; i++) {
            final Button button = new Button(this);
            button.setText("Button");
            button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT * i));
            button.setBackgroundColor(Color.parseColor("#237754"));

            viewGroup.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "button", Toast.LENGTH_SHORT).show();
                }
            });
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    animatedDelete((Button)v);
                    return true;
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void animatedDelete(final Button button) {
        final ViewGroupOverlay viewGroupOverlay = ((ViewGroup) findViewById(R.id.content)).getOverlay();
        viewGroupOverlay.add(button);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(button, "scaleX", 1, 3f),
                ObjectAnimator.ofFloat(button, "scaleY", 1, 3f),
                ObjectAnimator.ofFloat(button, "alpha", 1, 0.0f)
        );
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewGroupOverlay.remove(button);
            }
        });
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
