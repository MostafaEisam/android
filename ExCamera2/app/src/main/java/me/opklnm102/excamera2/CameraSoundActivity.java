package me.opklnm102.excamera2;

import android.annotation.TargetApi;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.MediaActionSound;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CameraSoundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_sound);
    }

    private MediaActionSound mMediaActionSound;

    private void openCamera(int width, int height){
        // CameraDevice객체를 생성하는 시점에 MediaActionSound객체 생성

        mMediaActionSound = new MediaActionSound();
        mMediaActionSound.load(MediaActionSound.SHUTTER_CLICK);
    }

    final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);

            //사진 이미지를 정상적으로 캡처하였다면 그 떄 셔터 소리를 재생
            mMediaActionSound.play(MediaActionSound.SHUTTER_CLICK);
            Toast.makeText(CameraSoundActivity.this, "Saved: " + file, Toast.LENGTH_SHORT).show();t
            startPreview();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if(mCameraDevice != null){
            mCameraDevice.close();
            mCameraDevice = null;
        }

        //재생에 사용한 MediaActionSound객체의 리소스 해제
        if(mMediaActionSound != null){
            mMediaActionSound.release();
            mMediaActionSound = null;
        }
    }
}
