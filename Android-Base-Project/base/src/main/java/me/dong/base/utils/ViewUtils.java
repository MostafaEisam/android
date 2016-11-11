package me.dong.base.utils;

import android.content.Context;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by opklnm102 on 2016-05-10.
 */
public class ViewUtils {

    @Dimension
    public static int pixelToDp(@NonNull Context context, @Px int pixel) {
        float dp = 0;
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            dp = pixel / (metrics.densityDpi / 160f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) dp;
    }

    @Px
    public static int dpToPixel(@NonNull Context context, @Dimension int dp) {
        float px = 0;
        try {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) px;
    }

    @Px
    public static int dpToPixel(@NonNull Context context, @Dimension float dp) {
        float px = 0;
        try {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) px;
    }
}
