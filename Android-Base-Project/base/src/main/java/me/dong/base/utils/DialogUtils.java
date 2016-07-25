package me.dong.base.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import me.dong.base.R;

public class DialogUtils {

    @Nullable
    private static ProgressDialog sProgressDialog;

    private DialogUtils() {
    }

    public static void showProgressDialog(@NonNull Context context) {
        showProgressDialog(context, R.string.please_wait);
    }

    public static void showProgressDialog(@NonNull Context context, @StringRes int resourceId) {
        showProgressDialog(context, context.getString(resourceId));
    }

    public static void showProgressDialog(@NonNull Context context, String message) {

        if (sProgressDialog != null) {
            sProgressDialog.setMessage(message);
            return;
        }

        sProgressDialog = new ProgressDialog(context);
        sProgressDialog.setCancelable(false);
        sProgressDialog.setMessage(message);
        sProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  //스피너 형태
        sProgressDialog.setCanceledOnTouchOutside(false);  //바깥쪽 눌러 종료 금지
        sProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (sProgressDialog != null) {
            sProgressDialog.dismiss();
        }
        sProgressDialog = null;
    }
}
