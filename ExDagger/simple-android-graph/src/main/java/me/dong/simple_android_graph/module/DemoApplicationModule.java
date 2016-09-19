package me.dong.simple_android_graph.module;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 애플리케이션 생명주기동안 살아있는 객체들을 제공
 * @Provide가 달린 모든 메소드에 @Singleton스코프를 사용하는 이유
 */
@Module
public class DemoApplicationModule {

    private final Application mApplication;

    public DemoApplicationModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) mApplication.getSystemService(Context.LOCATION_SERVICE);
    }
}
