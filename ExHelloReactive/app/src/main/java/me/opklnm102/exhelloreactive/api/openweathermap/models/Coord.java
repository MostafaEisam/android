package me.opklnm102.exhelloreactive.api.openweathermap.models;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016-05-12.
 */
public class Coord {

    @Expose
    private Double lon;

    @Expose
    private Double lat;

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
