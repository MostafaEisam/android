package me.dong.base.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.dong.base.utils.BusUtils;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusUtils.BUS.register(this);
    }

    @Override
    protected void onDestroy() {
        try {
            BusUtils.BUS.unregister(this);
        } catch (Exception ignore) {
        }
        super.onDestroy();
    }
}
