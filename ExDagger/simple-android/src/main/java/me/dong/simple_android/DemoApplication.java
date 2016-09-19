package me.dong.simple_android;

import android.app.Application;
import android.location.LocationManager;

import javax.inject.Inject;

import me.dong.simple_android.component.ApplicationComponent;
import me.dong.simple_android.component.DaggerApplicationComponent;
import me.dong.simple_android.module.AndroidModule;


public class DemoApplication extends Application {

    @Inject
    LocationManager mLocationManager;

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();

        component().inject(this);
    }

    public ApplicationComponent component() {
        return mApplicationComponent;
    }
}
