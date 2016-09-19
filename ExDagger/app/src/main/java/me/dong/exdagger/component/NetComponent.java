package me.dong.exdagger.component;

import javax.inject.Singleton;

import dagger.Component;
import me.dong.exdagger.module.AppModule;
import me.dong.exdagger.module.NetModule;
import me.dong.exdagger.ui.MainActivity;

/*
Module과 Inject간의 Bridge 역할, 의존성을 주입하는 역할
interface에만 사용 가능
컴포넌트를 구성하는 모든 @Module 클래스 목록을 적어야 한다.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(MainActivity activity);

    // void inject(MyFragment fragment);
    // void inject(MyService service);
}
