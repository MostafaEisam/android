package com.example.dong.eighthapp;

import java.util.Date;

/**
 * Created by Dong on 2015-07-17.
 */
/*
위치 정보를 받은 시작 일자와 현재 이동의 경과 시간을 위치 정보와 같이 보여주기 위한 클래스
 */
public class Run {
    private long mId;
    private Date mStartDate;

    public Run() {
        mId = -1;
        mStartDate = new Date();
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    //이동경과시간
    public int getDurationsSeconds(long endMillis) {
        return (int) ((endMillis - mStartDate.getTime()) / 1000);
    }

    public static String formatDuration(int durationSeconds) {
        int seconds = durationSeconds % 60;
        int minutes = ((durationSeconds - seconds) / 60) % 60;
        int hours = (durationSeconds - (minutes * 60) - seconds) / 3600;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
