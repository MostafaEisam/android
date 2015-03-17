package com.example.dong.doitmission_08;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Dong on 2015-03-10.
 */
public class MonthItemView extends TextView {
    private MonthItem item;

    public MonthItemView(Context context) {
        super(context);

        init();
    }

    public MonthItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init(){
        setBackgroundColor(Color.WHITE);
    }

    public MonthItem getItem(){
        return item;
    }

    public boolean getisPlan(){
        return item.getisPlan();
    }

    public void setisPlan(boolean bool){
        item.setisPlan(bool);
    }

    public void setItem(MonthItem item){
        this.item = item;

        int day = item.getDay();
        if(day != 0){
            setText(String.valueOf(day));
        }
        else{
            setText("");
        }
    }
}
