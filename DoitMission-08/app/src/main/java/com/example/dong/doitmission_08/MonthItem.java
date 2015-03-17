package com.example.dong.doitmission_08;

/**
 * Created by Dong on 2015-03-10.
 */
public class MonthItem {
    private int dayValue;
    boolean isPlan;  //일정이 있는지 없는지 유무

    //생성자
    public MonthItem() {
        isPlan = false;
    }

    //생성자
    public MonthItem(int day) {
        dayValue = day;
        isPlan = false;
    }

    public void setDay(int day) {
        this.dayValue = day;
    }

    public int getDay() {
        return dayValue;
    }

    public void setisPlan(boolean bool){
        isPlan = bool;
    }

    public boolean getisPlan(){
        return isPlan;
    }
}
