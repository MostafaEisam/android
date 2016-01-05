package com.example.dong.secondapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dong on 2015-07-08.
 */
public class Photo {
    private static final String JSON_FILENAME = "filename";

    private String mFilename;

    //디스크에 저장된 사진 파일을 나타내는 Photo 객체를 생성한다.
    public Photo(String filename) {
        mFilename = filename;
    }

    public Photo(JSONObject json) throws JSONException{
        mFilename = json.getString(JSON_FILENAME);
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        return json;
    }

    public String getFilename(){
        return mFilename;
    }
}
