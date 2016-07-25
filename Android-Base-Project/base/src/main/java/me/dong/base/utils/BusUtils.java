package me.dong.base.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public class BusUtils {

    //singleton
    public static final MainThreadBus BUS = new MainThreadBus();

    private BusUtils() {
    }

    public static class MainThreadBus extends Bus {
        private final Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void post(final Object event) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                super.post(event);
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainThreadBus.super.post(event);
                    }
                });
            }
        }
    }
}
