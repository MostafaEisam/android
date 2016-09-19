package me.dong.simple_android.component;

import javax.inject.Singleton;

import dagger.Component;
import me.dong.simple_android.DemoActivitiy;
import me.dong.simple_android.DemoApplication;
import me.dong.simple_android.ui.MainActivity;
import me.dong.simple_android.module.AndroidModule;

@Singleton
@Component(modules = AndroidModule.class)
public interface ApplicationComponent {
    void inject(DemoApplication application);

    void inject(MainActivity mainActivity);

    void inject(DemoActivitiy demoActivitiy);
}
