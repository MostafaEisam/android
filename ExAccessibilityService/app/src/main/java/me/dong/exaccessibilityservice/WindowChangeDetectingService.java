package me.dong.exaccessibilityservice;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * developer document
 * https://developer.android.com/reference/android/accessibilityservice/AccessibilityService.html?hl=ko
 */
public class WindowChangeDetectingService extends AccessibilityService implements TextToSpeech.OnInitListener{

    public static final String TAG = WindowChangeDetectingService.class.getSimpleName();

    public WindowChangeDetectingService() {
    }

    /*
    AccessibilityEvent를 detect했을 때 불리는 메소드
    여기서 event 처리
    life cycle동안 많이 호출되는 메소드
    Android system은 AccessibilityEvent를 통해 UI에 대한 정보 전달
    4.0이후부터 event.getRecordCount(),  event.getRecord(int)를 통해 AccessibilityRecord를 얻고
    event.getSource()를 통해 AccessibilityNodeInfo를 얻는다.

    event.getSource();
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //어느 앱이던이 앱이 실행되면 여기로 무조건 AccessibilityEvent가 들어온다.
        //여기서 해당앱인지 체크를 하고 앱이 맞으면 내가 설정한 앱을 띄워주면 되지 않을까요?
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.d(TAG, "Window package " + event.getPackageName());
        }

        AccessibilityNodeInfo source = event.getSource();
        if (source != null && source.getText() != null) {
            Log.d(TAG, source.toString());
            Log.d(TAG, source.getText().toString());
        }
    }

    /*
    필요시 구현
    accessibility service가 연결되었을 때 호출
    setServiceInfo()를 부르기 좋은 위치, service setup을 위해 1번 불린다.
    여기서 audio manager나 device vibrator들을 initialize를 한다.
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    /*
    system이 service가 하는 일을 멈추고 싶을 때 call
    보통 user가 하는 action에 대해 호출
    ex. focus 이동..
       life cycle동안 많이 호출되는 메소드
     */
    @Override
    public void onInterrupt() {

    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }

    /*
    필요시 구현
    accessibility service를 닫을 때 호출
    보통 resource 정리등에 쓰인다.
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
