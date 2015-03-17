package com.example.dong.doitmission_08;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.Calendar;

/**
 * Created by Dong on 2015-03-10.
 */
public class CalendarMonthAdapter extends BaseAdapter {
    public static final String TAG = "CalendarMonthAdapter";

    Context mContext;

    public static int oddColor = Color.rgb(225, 225, 225);
    public static int headColor = Color.rgb(12, 32, 158);

    private int selectedPosition = -1;

    private MonthItem[] items;

    private int countColumn = 7;

    int mStartDay;
    int startDay;
    int curYear;
    int curMonth;

    int firstDay;
    int lastDay;

    Calendar mCalendar;
    boolean recreateItems = false;

    public CalendarMonthAdapter(Context context) {
        super();

        mContext = context;
        init();
    }

    public CalendarMonthAdapter(Context mContext, Calendar mCalendar) {
        this.mContext = mContext;
        this.mCalendar = mCalendar;
    }

    private void init() {
        items = new MonthItem[7 * 6];

        mCalendar = Calendar.getInstance();
        recalculate();
        resetDayNumbers();
    }

    public void recalculate() {
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);  //날짜를 현재달의 1일로 설정

        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);  //현재요일을 얻는다.
        firstDay = getFirstDay(dayOfWeek);
        Log.d(TAG, "firstDay : " + firstDay);

        mStartDay = mCalendar.getFirstDayOfWeek();
        curYear = mCalendar.get(Calendar.YEAR);
        curMonth = mCalendar.get(Calendar.MONTH);
        lastDay = getMonthLastDay(curYear, curMonth);

        Log.d(TAG, "curYear : " + curYear + ", curMonth : " + curMonth + ", lastDay : " + lastDay);

        int diff = mStartDay - Calendar.SUNDAY - 1;
        startDay = getFirstDayOfWeek();
        Log.d(TAG, "mStartDay : " + mStartDay + ", startDay : " + startDay);
    }

    public void setPreviousMonth() {
        mCalendar.add(Calendar.MONTH, -1);
        recalculate();

        resetDayNumbers();
        selectedPosition = -1;
    }

    public void setNextMonth() {
        mCalendar.add(Calendar.MONTH, 1);
        recalculate();

        resetDayNumbers();
        selectedPosition = -1;
    }

    public void resetDayNumbers() {  //달력의 날짜를 셋팅
        for (int i = 0; i < 42; i++) {
            int dayNumber = (i + 1) - firstDay;
            if (dayNumber < 1 || dayNumber > lastDay) {
                dayNumber = 0;
            }
            items[i] = new MonthItem(dayNumber);
        }
    }

    private int getFirstDay(int dayOfWeek) {  //요일을 알아온다.
        int result = 0;

        if (dayOfWeek == Calendar.SUNDAY)
            result = 0;
        else if (dayOfWeek == Calendar.MONDAY)
            result = 1;
        else if (dayOfWeek == Calendar.TUESDAY)
            result = 2;
        else if (dayOfWeek == Calendar.WEDNESDAY)
            result = 3;
        else if (dayOfWeek == Calendar.THURSDAY)
            result = 4;
        else if (dayOfWeek == Calendar.FRIDAY)
            result = 5;
        else if (dayOfWeek == Calendar.SATURDAY)
            result = 6;
        return result;
    }

    public int getCurYear() {
        return curYear;
    }

    public int getCurMonth() {
        return curMonth;
    }

    public int getNumColumns() {
        return 7;
    }

    public int getCount() {
        return 7 * 6;
    }

    public Object getItem(int position) {
        return items[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView(" + position + ") called.");

        MonthItemView itemView;
        if (convertView == null)
            itemView = new MonthItemView(mContext);
        else
            itemView = (MonthItemView) convertView;

        GridView.LayoutParams params = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, 150);  //각셀의 크기 지정

        int rowIndex = position / countColumn;
        int columnIndex = position % countColumn;

        Log.d(TAG, "Index : " + rowIndex + ", " + columnIndex + " position : " + position);

        itemView.setItem(items[position]);
        itemView.setLayoutParams(params);
        itemView.setPadding(2, 2, 2, 2);

        itemView.setGravity(Gravity.LEFT);

        if (columnIndex == 0)  //날짜색 설정
            itemView.setTextColor(Color.RED);  //일요일 Red
        else if (columnIndex == 6)
            itemView.setTextColor(Color.BLUE);  //토요일 Blue
        else
            itemView.setTextColor(Color.BLACK);  //평일 Black

        //선택한 날짜 표시
        if (position == getSelectedPosition())  //현재 선택한 날짜 -> YELLOW
            itemView.setBackgroundColor(Color.YELLOW);
        else if (itemView.getisPlan() == true)  //일정있는 날짜 -> GRAY
            itemView.setBackgroundColor(Color.GRAY);
        else                                      //default 날짜 -> White
            itemView.setBackgroundColor(Color.WHITE);

        return itemView;
    }


    public static int getFirstDayOfWeek() {
        int startDay = Calendar.getInstance().getFirstDayOfWeek();  //한주의 첫번째 요일
        if (startDay == Calendar.SATURDAY) {
            return Time.SATURDAY;
        } else if (startDay == Calendar.MONDAY) {
            return Time.MONDAY;
        } else {
            return Time.SUNDAY;
        }
    }

    private int getMonthLastDay(int year, int month) {  //달의 마지막 일을 결정
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return (31);
            case 3:
            case 5:
            case 8:
            case 10:
                return (30);
            default:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return (29);  //2월 윤년계산
                } else {
                    return (28);
                }
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

}



