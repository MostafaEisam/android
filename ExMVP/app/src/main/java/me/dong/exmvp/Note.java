package me.dong.exmvp;

import java.util.Date;

/**
 * Created by Dong on 2016-07-25.
 */
public class Note {

    String text;
    Date date;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
