package me.dong.simple_android.ui;

import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import me.dong.simple_android.DemoActivitiy;
import me.dong.simple_android.DemoApplication;
import me.dong.simple_android.R;

public class MainActivity extends DemoActivitiy {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((DemoApplication) getApplication()).component().inject(this);

        Log.d(TAG, mLocationManager.toString());
    }
}
