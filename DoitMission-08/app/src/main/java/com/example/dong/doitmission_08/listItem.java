package com.example.dong.doitmission_08;

/**
 * Created by Dong on 2015-03-15.
 */
public class listItem {
    private String plan;  //일정
    private String time;  //시간


    public listItem(String time, String plan) {
        this.time = time;
        this.plan = plan;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}

