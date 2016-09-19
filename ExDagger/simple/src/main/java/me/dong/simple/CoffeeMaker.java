package me.dong.simple;


import javax.inject.Inject;

import dagger.Lazy;

public class CoffeeMaker {
    // Lazy Inject(사용할 때 inject)
    private final Lazy<Heater> heater;  // Create a possibly costly heater only when we use it.
    private final Pump pump;

    @Inject  //생성자 인자, 핃드 주입
    CoffeeMaker(Lazy<Heater> heater, Pump pump) {
        this.heater = heater;
        this.pump = pump;
    }

    public void brew() {
        heater.get().on();
        pump.pump();
        System.out.println(" [_]P coffee! [_]P ");
        heater.get().off();
    }
}
