package me.dong.exaccessibilityservice;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 현재 Activie된 app알아내기
 *
 * 관련 질문들
 * http://hashcode.co.kr/questions/1585/%EC%95%B1%EC%9E%A0%EA%B8%88-%EC%96%B4%ED%94%8C%EC%9D%84-%EB%A7%8C%EB%93%9C%EB%A0%A4%EB%8A%94-%EB%8C%80%ED%95%99%EC%83%9D%EC%9E%85%EB%8B%88%EB%8B%A4-%EB%8B%A4%EB%A5%B8%EC%95%B1%EC%9D%B4-%EC%BC%9C%EC%A7%80%EB%8A%94-%EA%B2%83%EC%9D%84-%EB%A7%89%EC%9D%84-%EB%B0%A9%EB%B2%95%EC%9C%BC%EB%A1%9C-%EC%96%B4%EB%96%A4-%EA%B2%83%EC%9D%B4-%EC%9E%88%EC%9D%84%EA%B9%8C%EC%9A%94
 * http://hashcode.co.kr/questions/1591/%ED%8A%B9%EC%A0%95-%EC%96%B4%ED%94%8C%EC%9D%B4-%EC%8B%A4%ED%96%89%EB%90%98%EB%A9%B4-%EC%A7%84%EC%A7%9C-%EC%A2%85%EB%A3%8C%EB%8A%94-%EC%95%84%EB%8B%88%EB%8D%94%EB%9D%BC%EB%8F%84-%EB%B9%84%EC%8A%B7%ED%95%9C-%ED%9A%A8%EA%B3%BC%EB%A5%BC-%EC%A4%84-%EC%88%98-%EC%97%86%EC%9D%84%EA%B9%8C%EC%9A%94
 *
 * Demo Project
 * https://github.com/leostc/AccessibilityDemo
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //접근성 권한 설정 바로가기
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, 0);
    }
}
