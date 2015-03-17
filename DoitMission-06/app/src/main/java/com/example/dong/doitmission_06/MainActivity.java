package com.example.dong.doitmission_06;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


public class MainActivity extends ActionBarActivity {
    private WebView webV;
    Button openBtn, closeBtn, moveBtn;
    EditText eT;
    boolean isPanelOpen = false;  //페널이 보이는지 여부

    Animation translateDownAnim, translateUpAnim;  //이동애니메이션 객체
    LinearLayout slidingPanel;  //슬라이딩으로 보여줄 패널

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webV = (WebView) findViewById(R.id.webView);
        openBtn = (Button) findViewById(R.id.openbutton);
        closeBtn = (Button) findViewById(R.id.closebutton);
        moveBtn = (Button) findViewById(R.id.movebutton);
        eT = (EditText) findViewById(R.id.editText);

        //슬라이딩 구현
        slidingPanel = (LinearLayout) findViewById(R.id.panel);
        translateDownAnim = AnimationUtils.loadAnimation(this, R.anim.translatedown);
        translateUpAnim = AnimationUtils.loadAnimation(this, R.anim.translateup);
        SlidingPanelAnimationListener animListener = new SlidingPanelAnimationListener();
        translateUpAnim.setAnimationListener(animListener);
        translateDownAnim.setAnimationListener(animListener);

        //웹뷰 구현
        WebSettings webSettings = webV.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webV.setWebViewClient(new WebViewClient());
        webV.loadUrl("http://www.google.com");

        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBtn.setVisibility(View.GONE);
                closeBtn.setVisibility(View.VISIBLE);
                slidingPanel.startAnimation(translateDownAnim);
                slidingPanel.setVisibility(View.VISIBLE);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBtn.setVisibility(View.VISIBLE);
                closeBtn.setVisibility(View.GONE);
                slidingPanel.startAnimation(translateUpAnim);
                slidingPanel.setVisibility(View.GONE);
            }
        });

        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String http = eT.getText().toString().substring(0,7);
                if(http.equals("http://")) {
                    webV.loadUrl(eT.getText().toString());
                }
                else{
                    String url = "http://" + eT.getText().toString();
                    webV.loadUrl(url);
                }
            }
        });
    }

    private class SlidingPanelAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
            if (isPanelOpen) {
                isPanelOpen = false;
            } else {
                isPanelOpen = true;
            }
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
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
