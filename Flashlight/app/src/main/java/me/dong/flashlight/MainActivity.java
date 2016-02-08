package me.dong.flashlight;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    ToggleButton tbFlash;
    Camera mCamera;
    Camera.Parameters mParameters;
    boolean isFlashOn;

    CameraDevice mCameraDevice;
    CameraManager mCameraManager;
    String[] mCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                mCameraId = mCameraManager.getCameraIdList();

                CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId[0]);

                boolean flashAvailable = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                if (flashAvailable) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Toast.makeText(MainActivity.this, "Flash is available", Toast.LENGTH_LONG).show();
//                    mCameraManager.openCamera(cameraId[0], new MyCameraDeviceStateCallback(), null);
                } else {
                    Toast.makeText(MainActivity.this, "Flash is not available", Toast.LENGTH_LONG).show();
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            initCamera();
        }

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
                Log.e(TAG, " camera open");
                mParameters = mCamera.getParameters();
            } catch (RuntimeException e) {
                Log.e(TAG, "Camera Failed to Open. " + e.getMessage());
//                finish();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mCameraDevice == null) {
                return;
            }
            mCameraDevice.close();
            mCameraDevice = null;
        } else {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }
    }

    private void turnOnFlash() {
        if (!isFlashOn) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mCameraDevice == null) {
                    return;
                }
                try {
                    mCameraManager.setTorchMode(mCameraId[0], true);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
//            mBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
//            try {
//                mCameraCaptureSession.setRepeatingRequest(mBuilder.build(), null, null);
//            } catch (CameraAccessException e) {
//                e.printStackTrace();
//            }
            } else {
                if (mCamera == null || mParameters == null) {
                    return;
                }
//            mParameters = mCamera.getParameters();
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(mParameters);
                mCamera.startPreview();
                isFlashOn = true;
            }
            Log.e(TAG, " turn On Flash");

            //play sound
//            playSound();
        }
    }

    private void turnOffFlash() {

        if (isFlashOn) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mCameraDevice == null) {
                    return;
                }
                try {
                    mCameraManager.setTorchMode(mCameraId[0], false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

//            mBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF);
//            try {
//                mCameraCaptureSession.setRepeatingRequest(mBuilder.build(), null, null);
//            } catch (CameraAccessException e) {
//                e.printStackTrace();
//            }
            } else {
                if (mCamera == null || mParameters == null) {
                    return;
                }
//            mParameters = mCamera.getParameters();
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(mParameters);
                mCamera.stopPreview();
                isFlashOn = false;
            }
            Log.e(TAG, " turn Off Flash");

            //play sound
//            playSound();
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

//    class MyCameraDeviceStateCallback extends CameraDevice.StateCallback {
//
//        @Override
//        public void onOpened(CameraDevice camera) {
//            mCameraDevice = camera;
//
//            //get builder
//            try {
//                mBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_MANUAL);
//
//                //flash on, default is on
//                mBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO);
//                mBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
//                List<Surface> surfaceList = new ArrayList<>();
//                mSurfaceTexture = new SurfaceTexture(1);
//                Size size = getSmallSize(mCameraDevice.getId());
//                mSurfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());
//                mSurface = new Surface(mSurfaceTexture);
//                surfaceList.add(mSurface);
//                mBuilder.addTarget(mSurface);
//                camera.createCaptureSession(surfaceList, new MyCameraCaptureSessionStateCallback(), null);
//            } catch (CameraAccessException e) {
//                e.printStackTrace();
//            }
//        }

//        @Override
//        public void onDisconnected(CameraDevice camera) {
//        }
//
//        @Override
//        public void onError(CameraDevice camera, int error) {
//        }
//    }

//    class MyCameraCaptureSessionStateCallback extends CameraCaptureSession.StateCallback {
//
//        @Override
//        public void onConfigured(CameraCaptureSession session) {
//            mCameraCaptureSession = session;
//
//            try {
//                mCameraCaptureSession.setRepeatingRequest(mBuilder.build(), null, null);
//            } catch (CameraAccessException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onConfigureFailed(CameraCaptureSession session) {
//        }
//    }
}
/**
    참고 URL
    https://github.com/googlesamples/android-Camera2Basic/tree/master/Application
    http://developer.android.com/intl/ko/reference/android/hardware/camera2/CameraManager.TorchCallback.html
    https://github.com/pinguo-yuyidong/Camera2/blob/master/app/src/main/java/us/yydcdut/androidltest/otheractivity/FlashActivity.java
    https://www.google.co.kr/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q=android+flashlight+camera2&newwindow=1&safe=off&tbs=qdr:m
 **/
