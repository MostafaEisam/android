package me.opklnm102.excamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016-04-18.
 */
public class Preview extends SurfaceView implements SurfaceHolder.Callback {

    public static final String TAG = Preview.class.getSimpleName();

    SurfaceHolder mSurfaceHolder;
    Camera.Size mPreviewSize;
    List<Camera.Size> mSupportedPreviewSizes;
    Camera mCamera;

    public Preview(Context context, Camera camera) {
        super(context);

        //SurfaceView클래스는 getHolder()를 제공
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        mCamera = camera;
        if(mCamera != null){
            requestLayout();
            Camera.Parameters parameters = mCamera.getParameters();

            //카메라가 오토포커스를 지원하는지 여부 확인
            List<String> focusModes = parameters.getSupportedFocusModes();
            if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)){
                parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);

                //오토포커스가 가능하도록 카메라 매개변수를 설정
                mCamera.setParameters(parameters);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //서피스매니저 프레임에 의해 화면이 열려지면 해당 콜백메소드가 호출
        try {
            if(mCamera != null){
                mCamera.setPreviewDisplay(mSurfaceHolder);
            }
        }catch (IOException e){
            Log.e(TAG, " IOException caused by setPreviewDisplay()", e);
        }

    }

    //뷰의 화면크기가 변화되었을 때 호출
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(mSurfaceHolder.getSurface() == null){
            return;
        }

        try {
            mCamera.stopPreview();
        }catch (Exception e){
            return;
        }

        try{
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        }catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        //화면이 닫히면 미리보기 해제
        if(mCamera != null){
            mCamera.stopPreview();
        }

    }
}
