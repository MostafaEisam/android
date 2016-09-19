package me.dong.exdagger.common;

import android.app.Application;

import me.dong.exdagger.component.DaggerNetComponent;
import me.dong.exdagger.component.NetComponent;
import me.dong.exdagger.module.AppModule;
import me.dong.exdagger.module.NetModule;


public class MyApplication extends Application {

    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
        mNetComponent = DaggerNetComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this))
                .netModule(new NetModule("http://api.github.com"))
                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mNetComponent = com.codepath.dagger.components.DaggerNetComponent.create();
    }

    public NetComponent getNetComponent(){
        return mNetComponent;
    }
}
