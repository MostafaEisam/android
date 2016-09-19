package me.dong.simple;

import javax.inject.Inject;

public class Thermosiphon implements Pump{

    private final Heater mHeater;

    @Inject  //생성자 인자, 핃드 주입
    public Thermosiphon(Heater heater){
        this.mHeater = heater;
    }

    @Override
    public void pump() {
        if (mHeater.isHot()) {
            System.out.println("=> => pumping => =>");
        }
    }
}
