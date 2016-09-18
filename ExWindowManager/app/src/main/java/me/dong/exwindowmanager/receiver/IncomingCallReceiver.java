package me.dong.exwindowmanager.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import me.dong.exwindowmanager.service.CallingService;

/**
 * 전화가 걸려오는경우를 감지하고 전화번호 팝업으로 띄우기
 */
public class IncomingCallReceiver extends BroadcastReceiver {

    public static final String TAG = IncomingCallReceiver.class.getSimpleName();
    private static String mLastState;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public IncomingCallReceiver() {
    }

    @RequiresPermission(value = Manifest.permission.READ_PHONE_STATE)
    @Override
    public void onReceive(Context context, Intent intent) {

        /**
         * http://mmarvick.github.io/blog/blog/lollipop-multiple-broadcastreceiver-call-state/
         * 2번 호출되는 문제 해결
         */
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.d(TAG, " onReceive() " + state);

        if (state.equals(mLastState)) {
            return;
        } else {
            mLastState = state;
        }

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);  //format 전 번호
            Log.d(TAG, " onReceive() " + incomingNumber);
            String incomingNumberFormated;  //format 후 번호

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //https://developer.android.com/reference/android/telephony/PhoneNumberUtils.html#formatNumber(java.lang.String, java.lang.String)
                // 410 KOR KR
                incomingNumberFormated = android.telephony.PhoneNumberUtils.formatNumber(incomingNumber, "KR");
            } else {
                incomingNumberFormated = android.telephony.PhoneNumberUtils.formatNumber(incomingNumber);
            }
            Log.d(TAG, " onReceive() " + incomingNumberFormated);

            //http://stackoverflow.com/questions/37982167/android-permission-denied-for-window-type-2010-in-marshmallow-or-higher
            //http://stackoverflow.com/questions/7569937/unable-to-add-window-android-view-viewrootw44da9bc0-permission-denied-for-t
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(context)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    startCallingService(context, incomingNumberFormated);
                }
            } else {
                startCallingService(context, incomingNumberFormated);
            }
        } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            Intent serviceIntent = new Intent(context, CallingService.class);
            context.stopService(serviceIntent);
        }

//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        telephonyManager.listen(new CustomPhoneStateListener(context), PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void startCallingService(Context context, String phoneNumber) {
        Intent serviceIntent = new Intent(context, CallingService.class);
        serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phoneNumber);
        context.startService(serviceIntent);
    }

//    class CustomPhoneStateListener extends PhoneStateListener {
//
//        private Context mContext;
//
//        public CustomPhoneStateListener(Context context) {
//            super();
//            this.mContext = context;
//        }
//
//        @Override
//        public void onCallStateChanged(int state, String incomingNumber) {
//
//            switch (state) {
//                case TelephonyManager.CALL_STATE_IDLE:
//                    Log.e(TAG, " CALL_STATE_IDLE " + incomingNumber);
//                    break;
//                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    Log.e(TAG, " CALL_STATE_OFFHOOK " + incomingNumber);
//                    break;
//                case TelephonyManager.CALL_STATE_RINGING:
//                    Log.e(TAG, " CALL_STATE_RINGING " + incomingNumber);
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
}
