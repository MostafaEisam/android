package me.dong.rxandroidex;

import io.realm.RealmObject;

/**
 * Created by Dong on 2016-02-19.
 */
public class Person extends RealmObject {

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
