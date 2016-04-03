package me.opklnm102.studyexampleproject.models;

/**
 * Created by Administrator on 2016-04-03.
 */
public class ImageItem {

    Integer imgId;

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public ImageItem(Integer imgId) {
        this.imgId = imgId;
    }

    public ImageItem() {
        this.imgId = null;
    }
}
