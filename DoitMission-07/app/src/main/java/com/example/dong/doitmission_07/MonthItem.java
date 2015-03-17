package com.example.dong.doitmission_07;

/**
 * Created by Dong on 2015-03-10.
 */
public class MonthItem {
    private int dayValue;

    public MonthItem() {
    }

    public MonthItem(int day) {
        dayValue = day;
    }

    public void setDay(int day) {
        this.dayValue = day;
    }

    public int getDay() {
        return dayValue;
    }
}
