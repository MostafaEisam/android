package me.dong.recyclerviewstudy.event;

import com.squareup.otto.Bus;

/**
 * Created by Dong on 2017-01-12.
 */

public class BusProvider {

    private static Bus instance;

    public static Bus getInstance() {
        if (instance == null) {
            synchronized (BusProvider.class) {
                if (instance == null) {
                    instance = new Bus();
                }
            }
        }
        return instance;
    }

    private BusProvider(){
    }
}
