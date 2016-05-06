package me.opklnm102.exhelloreactive.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by Administrator on 2016-05-06.
 */
public class AppInfoRich implements Comparable<Object> {

    String mName;

    private Context mContext;

    private ResolveInfo mResolveInfo;

    private ComponentName mComponentName = null;

    private PackageInfo mPackageInfo = null;

    private Drawable icon = null;

    public AppInfoRich(Context context, ResolveInfo ri){
        mContext = context;
        mResolveInfo = ri;

        mComponentName = new ComponentName(ri.activityInfo.applicationInfo.packageName, ri.activityInfo.name);

        try {
            mPackageInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getActivityName(){
        return mResolveInfo.activityInfo.name;
    }

    public String getName() {
        if(mName != null){
            return mName;
        }else {
            try {
                return getNameFromResolveInfo(mResolveInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return getPackageName();
            }
        }
    }

    public String getComponentInfo(){
        if (getComponentName() != null){
            return getComponentName().toString();
        }else {
            return "";
        }
    }

    public String getVersionName(){
        PackageInfo pi = getPackageInfo();
        if(pi != null){
            return pi.versionName;
        }else {
            return "";
        }
    }

    public int getVersionCode(){
        PackageInfo pi = getPackageInfo();
        if(pi != null){
            return pi.versionCode;
        }else {
            return 0;
        }
    }

    public String getPackageName(){
        return mResolveInfo.activityInfo.packageName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public ResolveInfo getResolveInfo() {
        return mResolveInfo;
    }

    public void setResolveInfo(ResolveInfo resolveInfo) {
        mResolveInfo = resolveInfo;
    }

    public ComponentName getComponentName() {
        return mComponentName;
    }

    public void setComponentName(ComponentName componentName) {
        mComponentName = componentName;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        mPackageInfo = packageInfo;
    }

    public Drawable getIcon() {
        if(icon == null){
            icon = getResolveInfo().loadIcon(mContext.getPackageManager());
        }
        return icon;
    }

    public long getFirstInstallTime(){
        PackageInfo pi = getPackageInfo();
        if(pi != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            return pi.firstInstallTime;
        }else {
            return 0;
        }
    }

    public long getLastUpdateTime(){
        PackageInfo pi = getPackageInfo();
        if(pi != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            return pi.lastUpdateTime;
        }else {
            return 0;
        }
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Helper method to get an applications name!
     */
    public String getNameFromResolveInfo(ResolveInfo ri) throws PackageManager.NameNotFoundException {
        String name = ri.resolvePackageName;
        if(ri.activityInfo != null){
            Resources res = mContext.getPackageManager().getResourcesForApplication(ri.activityInfo.applicationInfo);
            Resources engRes = getEnglishRessources(res);

            if(ri.activityInfo.labelRes != 0){
                name = engRes.getString(ri.activityInfo.labelRes);

                if(name == null || name.equals("")){
                    name = res.getString(ri.activityInfo.labelRes);
                }
            }else {
                name = ri.activityInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
            }
        }
        return name;
    }

    public Resources getEnglishRessources(Resources resources){
        AssetManager assets = resources.getAssets();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration config = new Configuration(resources.getConfiguration());
        config.locale = Locale.US;
        return new Resources(assets, metrics, config);
    }

    @Override
    public int compareTo(Object another) {
       AppInfoRich f = (AppInfoRich) another;
        return getName().compareTo(f.getName());
    }
}
