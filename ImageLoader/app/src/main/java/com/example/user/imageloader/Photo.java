package com.example.user.imageloader;

import java.io.Serializable;

/**
 * Created by User on 2015-05-14.
 */
public class Photo implements Serializable{
    String title;
    String imageUrl;

    public Photo(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
