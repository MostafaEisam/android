package me.dong.simple_android.module;


import android.content.Context;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.dong.simple_android.DemoApplication;
import me.dong.simple_android.ForApplication;

@Module
public class AndroidModule {

    private final DemoApplication mApplication;

    public AndroidModule(DemoApplication application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) mApplication.getSystemService(Context.LOCATION_SERVICE);
    }
}
