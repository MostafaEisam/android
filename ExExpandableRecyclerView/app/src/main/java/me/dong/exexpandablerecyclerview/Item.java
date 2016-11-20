package me.dong.exexpandablerecyclerview;

import java.util.List;

/**
 * Created by Dong on 2016-11-20.
 */

public class Item {
    public int type;
    public String text;
    public List<Item> invisibleChildren;

    public Item() {
    }

    public Item(int type, String text) {
        this.type = type;
        this.text = text;
    }
}
