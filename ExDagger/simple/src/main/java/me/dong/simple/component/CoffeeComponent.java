package me.dong.simple.component;


import javax.inject.Singleton;

import dagger.Component;
import me.dong.simple.CoffeeMaker;
import me.dong.simple.module.DripCoffeeModule;

@Singleton
@Component(modules = {DripCoffeeModule.class})  //@Module과 @Inject간의 Bridge같은 역할
public interface CoffeeComponent {
    CoffeeMaker maker();
}
