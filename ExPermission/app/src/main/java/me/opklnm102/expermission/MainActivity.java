package me.opklnm102.expermission;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
    API 23부터 퍼미션 모델 변한
    실행중에 특정 기능을 사용할 때마다 사용자의 허가를 일일이 받아야 한다.

    1. 일반 퍼미션
    App 외부의 데이터나 기능을 엑세스하지만 위험X
    지금까지의 퍼미션 정책 사용
    ex) flash 등

    2. 위험 퍼미션
    사용자의 사적인 정보 엑세스하는 기능은 개인정보가 유출될 수 있으므로 위험
    실행중 일일이 허가
    ex) 주소록, 통화목록, GPS 등

    개별 퍼미션마다 요청하면 귀찮으니까 퍼미션 그룹을 사용
    http://developer.android.com/intl/in/reference/android/Manifest.permission_group.html

    CONTACTS    READ_CONTACTS
                WRITE_CONTACTS
                GET_ACCOUNTS
    PHONE       READ_PHONE_STATE
                CALL_PHONE
                READ_CALL_LOG
                WRITE_CALL_LOG
                ADD_VOICEMAIL
                USE_SIP
                PROCESS_OUTGOING_CALLS
    SMS         SEND_SMS
                RECEIVE_SMS
                READ_SMS
                RECEIVE_WAP_PUSH
                RECEIVE_MMS
    STORAGE     READ_EXTERNAL_STORAGE
                WRITE_EXTERNAL_STORAGE
    LOCATION    ACCESS_FINE_LOCATION
                ACCESS_COARSE_LOCATION
    CALENDAR    READ_CALENDAR
                WRITE_CALENDAR
    CAMERA      CAMERA
    MICROPHONE  RECORD_AUDIO
    SENSORS     BODY_SENSORS
     */
public class MainActivity extends AppCompatActivity {

    final int READ_CONTACT_CODE = 0;

    TextView tvResult;
    Button btnRead, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = (TextView) findViewById(R.id.textView_result);
        btnRead = (Button) findViewById(R.id.button_read);
        btnReset = (Button) findViewById(R.id.button_reset);

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryOutContact();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText("초기화");
            }
        });
    }

    /**
     * 퍼미션 순서도
     *
     * 퍼미션이 있는가?   --- No --->  퍼미션 설명 및 요청
     *
     *       l                              ㅣ
     *       l                              ㅣ
     *      Yes                             ㅣ
     *       l                              ㅣ
     *       l                              ㅣ
     *
     *   기능 실행    <--- Yes ---      사용자 허가?
     *
     *                                      l
     *                                      l
     *                                      No
     *                                      l
     *                                      l
     *
     *                                 다른 방법 실행
     *                                 또는 에러처리
     */
    public void tryOutContact() {
        /*
         ContextCompat.checkSelfPermission(Context context, String permission)
         (액티비티, 조사할 퍼미션 이름)
         퍼미션을 가지고 있으면 PackageManager.PERMISSION_GRANTED 리턴
         없으면 PackageManager.PERMISSION_DENIED
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "허가된 상태", Toast.LENGTH_SHORT).show();
            outContact();
        } else {
            /*
            사용자에게 퍼미션에 대해 설명
            최소 1번정도 거절했을 때 도움말 출력
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)는 모든 상황을보고 설명이 필요한 시점이면 true 반환
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                new AlertDialog.Builder(this)
                        .setMessage("이 프로그램이 원활하게 동작하기 위해서는 " +
                                "주소록 읽기 퍼미션이 꼭 필요합니다." +
                                "퍼미션을 허가해 주세요.")
                        .setTitle("제발~~")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_CODE);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            } else {
                Toast.makeText(MainActivity.this, "허가된 상태가 아니어서 퍼미션 요청", Toast.LENGTH_SHORT).show();

                 /*
            ActivityCompat.requestPermissions(Activity activity, String[] permissions, int requestCode)
            필요한 퍼미션을 한꺼번에 요청하는데 2번째 인수로 머시션의 목록을 배열로 전달
            요청할 퍼미션은 매니페스트에도 반드시 기록되어 있어야 한다.
             */
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_CODE);
            }
        }
    }

    /*
    (요청코드, 퍼미션 목록, 허가 여부)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case READ_CONTACT_CODE:
                //사용자가 무시하거나 강제로 닫으면 빈배열 전달. null이 아니지만 길이가 0일 수 있으므로 반드시 점검.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "사용자가 퍼미션 허가함", Toast.LENGTH_SHORT).show();
                    outContact();
                } else {
                    Toast.makeText(MainActivity.this, "사용자가 퍼미션 거부함", Toast.LENGTH_SHORT).show();
                    //Todo: 다른 대첵을 찾거나 에러처리
                }
        }
    }

    public void outContact() {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int nameidx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

        if (cursor.moveToNext()) {
            tvResult.setText(cursor.getString(nameidx));
        } else {
            tvResult.setText("주소록이 비었다.");
        }
        cursor.close();
    }
}
