package me.dong.exdagger.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;
import javax.inject.Named;

import me.dong.exdagger.common.MyApplication;
import me.dong.exdagger.R;
import okhttp3.OkHttpClient;

/*
http://guides.codepath.com/android/Dependency-Injection-with-Dagger-2
https://medium.com/@jason_kim/tasting-dagger-2-on-android-%EB%B2%88%EC%97%AD-632e727a7998#.qhfnmul74
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Inject  //객체 주입
    SharedPreferences mSharedPreferences;

    @Inject
    @Named("cached")  //이름이 같은게 주입됨
    OkHttpClient mOkHttpClient;

    @Inject
    @Named("non_cached")
    OkHttpClient mOkHttpClient2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyApplication) getApplication()).getNetComponent().inject(this);
    }
}
