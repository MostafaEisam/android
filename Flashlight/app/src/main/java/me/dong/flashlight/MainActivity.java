package me.dong.flashlight;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    ToggleButton tbFlash;
    Camera mCamera;
    Camera.Parameters mParameters;
    boolean isFlashOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCamera();

        tbFlash = (ToggleButton) findViewById(R.id.toggleButton);
        tbFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    turnOnFlash();
                } else {
                    turnOffFlash();
                }
            }
        });
    }

    //getting camera parameters
    private void initCamera() {
        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                mParameters = mCamera.getParameters();
            } catch (RuntimeException e) {
                Log.e(TAG, "Camera Failed to Open. " + e.getMessage());
                try {
                    mCamera.reconnect();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    finish();
                }
            }
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void turnOnFlash() {
        if (!isFlashOn) {
            if (mCamera == null || mParameters == null) {
                return;
            }

            Log.e(TAG, " turn On Flash");

            //play sound
//            playSound();

//            mParameters = mCamera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(mParameters);
            mCamera.startPreview();
            isFlashOn = true;
        }
    }

    private void turnOffFlash() {
        if (isFlashOn) {
            if (mCamera == null || mParameters == null) {
                return;
            }

            Log.e(TAG, " turn Off Flash");

            //play sound
//            playSound();

//            mParameters = mCamera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(mParameters);
            mCamera.stopPreview();
            isFlashOn = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        turnOffFlash();
        releaseCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        turnOffFlash();
        releaseCamera();
        super.onDestroy();
    }
}
