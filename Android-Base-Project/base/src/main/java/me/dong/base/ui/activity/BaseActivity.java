package me.dong.base.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.dong.base.utils.BusUtils;

public abstract class BaseActivity extends AppCompatActivity {

    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusUtils.BUS.register(this);

        mUnbinder = ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();

        try {
            BusUtils.BUS.unregister(this);
        } catch (Exception ignore) {
        }
        super.onDestroy();
    }

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initFragment(Fragment fragment);
}
