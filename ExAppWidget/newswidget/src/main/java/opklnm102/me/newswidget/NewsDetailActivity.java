package opklnm102.me.newswidget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Intent intent = getIntent();
        int newsid = intent.getIntExtra("newsid", 100);
        Log.d(NewsWidget.TAG, "Config news id = " + newsid);
        TextView tvDetail = (TextView) findViewById(R.id.textView_detail);
        tvDetail.setText("상세 뉴스 보기 : " + newsid);
    }
}
