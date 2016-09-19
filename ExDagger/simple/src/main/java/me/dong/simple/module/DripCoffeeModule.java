package me.dong.simple.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.dong.simple.ElectricHeater;
import me.dong.simple.Heater;

@Module(includes = PumpModule.class)  //Module로 구성되고, 내부에서 주입될 객체들을 provide 해준다.
public class DripCoffeeModule {

    @Provides  //주입할 대상을 제공, Module에 속해야 한다.
    @Singleton  //싱글톤으로 관리
    Heater provideHeater() {
        return new ElectricHeater();
    }
}
