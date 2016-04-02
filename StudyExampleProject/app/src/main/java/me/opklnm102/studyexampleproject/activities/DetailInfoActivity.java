package me.opklnm102.studyexampleproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import me.opklnm102.studyexampleproject.R;

public class DetailInfoActivity extends AppCompatActivity {

    Toolbar mToolbar;

    ImageView ivProfile;
    TextView tvName;
    TextView tvPhoneNumber;
    Button btnPhoneCall;
    Button btnMessage;
    Button btnFinish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        initView();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();






    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ivProfile = (ImageView) findViewById(R.id.imageView_profile);
        tvName = (TextView) findViewById(R.id.textView_name);
        tvPhoneNumber = (TextView) findViewById(R.id.textView_phone_number);
        btnPhoneCall = (Button) findViewById(R.id.button_phone_call);
        btnMessage = (Button) findViewById(R.id.button_message);
        btnFinish = (Button) findViewById(R.id.button_finish);
    }




}
