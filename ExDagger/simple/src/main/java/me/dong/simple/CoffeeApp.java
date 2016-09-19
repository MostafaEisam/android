package me.dong.simple;

import me.dong.simple.component.CoffeeComponent;
import me.dong.simple.component.DaggerCoffeeComponent;

public class CoffeeApp {

    public static void main(String[] args) {
        CoffeeComponent coffeeComponent = DaggerCoffeeComponent.builder().build();
        coffeeComponent.maker().brew();


    }
}
