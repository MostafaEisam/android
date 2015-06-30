package com.example.dong.ewhakatalk_dongheekim;

/**
 * Created by User on 2015-06-03.
 */
public class Profile {
    private String name;  //이름
    private String imgUrl;  //프사
    private String stateMsg;  //상태메시지
    private boolean bookMark;  //즐겨찾기

    public Profile(String name, String imgUrl, String stateMsg) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.stateMsg = stateMsg;
        bookMark = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStateMsg() {
        return stateMsg;
    }

    public void setStateMsg(String stateMsg) {
        this.stateMsg = stateMsg;
    }

    public boolean isBookMark() {
        return bookMark;
    }

    public void setBookMark(boolean bookMark) {
        this.bookMark = bookMark;
    }
}
