package me.dong.simple_android_graph.component;

import android.app.Application;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Component;
import me.dong.simple_android_graph.DemoApplication;
import me.dong.simple_android_graph.module.DemoApplicationModule;

/**
 * 애플리케이션의 수명이 컴포넌트의 수명
 * Application과 HomeActivity에 주입
 */
@Singleton
@Component(modules = DemoApplicationModule.class)
public interface ApplicationComponent {
    // Field injections of any dependencies of the DemoApplication
    void inject(DemoApplication application);

    // Exported for child-components.
    // Dagger Component가 어떻게 동작하는지에 대한 중요한 property
    // 명시적으로 이용하지 않을거라면 모듈로부터 해당 타입 공개X
    Application application();

    LocationManager locationManager();
}


