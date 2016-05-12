package me.opklnm102.exhelloreactive.api.openweathermap.models;

import android.content.Intent;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016-05-12.
 */
public class Clouds {

    @Expose
    private Integer all;

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }
}
