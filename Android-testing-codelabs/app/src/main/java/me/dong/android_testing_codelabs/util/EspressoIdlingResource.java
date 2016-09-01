package me.dong.android_testing_codelabs.util;

import android.support.test.espresso.IdlingResource;

/**
 * Contains a static reference to {@link IdlingResource}, only available in the 'mock' build type.
 */
public class EspressoIdlingResource {

    private static final String RESOURCE = "GLOBAL";

    private static SimpleCountingIdlingResource sSimpleCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);

    public static void increment(){
        sSimpleCountingIdlingResource.increment();
    }

    public static void decrement(){
        sSimpleCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource(){
        return sSimpleCountingIdlingResource;
    }
}
