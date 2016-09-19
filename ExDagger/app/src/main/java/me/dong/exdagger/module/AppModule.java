package me.dong.exdagger.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/*
의존성을 제공하는 클래스를 정의하면
Dagger는 클래스 인스턴스를 만들 때 의존성을 만족시키기 위한 정보를 찾을 수 있다.
 */
@Module  //내부에서 주입될 객체들을 provide 해준다.
public class AppModule {

    Application mApplication;

    public AppModule(Application application){
        this.mApplication = application;
    }

    @Provides  //객체 주입에 필요한 내용을 리턴해준다
    @Singleton
    Application provideApplication(){
        return mApplication;
    }
}
