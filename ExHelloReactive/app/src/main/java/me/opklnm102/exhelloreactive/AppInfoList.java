package me.opklnm102.exhelloreactive;

import java.util.List;

import me.opklnm102.exhelloreactive.model.AppInfo;

/**
 * Created by Administrator on 2016-05-06.
 */
public class AppInfoList {

    private static AppInfoList instance = new AppInfoList();

    private List<AppInfo> mAppInfoList;

    public List<AppInfo> getAppInfoList() {
        return mAppInfoList;
    }

    public void setAppInfoList(List<AppInfo> appInfoList) {
        mAppInfoList = appInfoList;
    }

    private AppInfoList(){

    }

    public static AppInfoList getInstance(){
        return instance;
    }
}
