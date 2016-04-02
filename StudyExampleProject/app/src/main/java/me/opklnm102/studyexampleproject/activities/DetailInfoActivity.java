package me.opklnm102.studyexampleproject.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

    String strName;
    String strPhoneNumber;
    Integer profileImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        initView();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent receiveIntent = getIntent();


//        ivProfile
//                tvName
//        tvPhoneNumber

        btnPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + strName)));
            }
        });

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + strPhoneNumber)));
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
