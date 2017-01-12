package me.dong.recyclerviewstudy.factory;

import me.dong.recyclerviewstudy.common.Constants;
import me.dong.recyclerviewstudy.model.ABox;
import me.dong.recyclerviewstudy.model.ImageBox;
import me.dong.recyclerviewstudy.model.TextBox;


public class BoxFactory {

    public static ABox getBox(BoxType type) {
        ABox aBox = null;
        switch (type) {
            case IMAGE:
                aBox = new ImageBox();
                aBox.setViewType(Constants.VIEW_TYPE_IMAGE);
                break;
            case TEXT:
                aBox = new TextBox();
                aBox.setViewType(Constants.VIEW_TYPE_TEXT);
                break;
        }
        return aBox;
    }

    public enum BoxType {
        TEXT,
        IMAGE
    }
}
