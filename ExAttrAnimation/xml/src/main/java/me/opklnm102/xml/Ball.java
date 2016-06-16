package me.opklnm102.xml;

import android.graphics.Color;

/**
 * Created by Administrator on 2016-06-14.
 */
public class Ball {

    int x = 0;
    int y = 0;
    int radius = 20;
    int color = Color.YELLOW;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
