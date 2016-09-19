package me.dong.notificationcapture;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * developer documents
 * https://developer.android.com/reference/android/accessibilityservice/AccessibilityService.html?hl=ko
 */
public class NotificationCaptureService extends AccessibilityService {

    public static final String TAG = NotificationCaptureService.class.getSimpleName();

    String inputText = null;
    int prevEvent = 0;

    public NotificationCaptureService() {
    }

    //private static final String[] PACKAGE_NAMES = new String[] {  };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "Catch Event : " + event.toString());

     /*
       아래와 같은 이벤트, 값들을 수집 가능.
       AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
       AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
       AccessibilityEvent.TYPE_VIEW_CLICKED
      */
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
                && event.getPackageName() == "com.kakao.talk") {
            inputText = event.getText().toString();
        }
        Log.d(TAG, "notification - " + inputText);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "OnInterrupt");
    }

    public void onServiceConnected() {
        Log.d(TAG, "onServiceConnected!!");

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        // We are interested in all types of accessibility events.
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        // We want to provide specific type of feedback.
        info.feedbackType = AccessibilityServiceInfo.DEFAULT | AccessibilityServiceInfo.FEEDBACK_HAPTIC;
//       info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
        // We want to receive events in a certain interval.
        info.notificationTimeout = 100;
        // We want to receive accessibility events only from certain packages.
        // info.packageNames = PACKAGE_NAMES;
        setServiceInfo(info);
    }
}