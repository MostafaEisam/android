package me.dong.recyclerviewstudy.event;

import me.dong.recyclerviewstudy.factory.BoxFactory;

/**
 * Created by Dong on 2017-01-12.
 */

public class BoxAddEvent {

    private BoxFactory.BoxType mBoxType;

    public BoxAddEvent(BoxFactory.BoxType  boxType) {
        mBoxType = boxType;
    }

    public BoxFactory.BoxType  getBoxType() {
        return mBoxType;
    }

    public void setBoxType(BoxFactory.BoxType  boxType) {
        mBoxType = boxType;
    }
}
