package me.dong.simple_android_graph;

import android.app.Application;
import android.location.LocationManager;

import javax.inject.Inject;

import me.dong.simple_android_graph.component.ApplicationComponent;
import me.dong.simple_android_graph.component.DaggerApplicationComponent;
import me.dong.simple_android_graph.module.DemoApplicationModule;


public class DemoApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Inject
    LocationManager mLocationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .demoApplicationModule(new DemoApplicationModule(this))
                .build();
    }

    public ApplicationComponent component() {
        return mApplicationComponent;
    }
}
