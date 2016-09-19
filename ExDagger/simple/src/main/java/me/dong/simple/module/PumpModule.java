package me.dong.simple.module;

import dagger.Binds;
import dagger.Module;
import me.dong.simple.Pump;
import me.dong.simple.Thermosiphon;

@Module
public abstract class PumpModule {

    @Binds
    abstract Pump providePump(Thermosiphon pump);
}
