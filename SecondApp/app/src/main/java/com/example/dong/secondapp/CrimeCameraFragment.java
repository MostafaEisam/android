package com.example.dong.secondapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dong on 2015-07-03.
 */
public class CrimeCameraFragment extends Fragment {
    private static final String TAG = "CrimeCameraFragment";

    public static final String EXTRA_PHOTO_FILENAME = "com.example.dong.secondapp.photo_filename";

    private Camera mCamera;
    private SurfaceView mSurfaceView;  //라이브 미리보기
    private View mProgressContainer;

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            //프로그래스 표시기를 보여준다.
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //파일 이름을 만든다.
            String fileName = UUID.randomUUID().toString() + ".jpg";  //파일명으로 사용할 고유한 문자열 생성
            //jpeg 데이터를 디스크에 저장한다.
            FileOutputStream os = null;
            boolean success = true;

            try {
                os = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                os.write(data);
            } catch (IOException e) {
                Log.e(TAG, "Error writing to file " + fileName, e);
                success = false;
            }finally {
                try{
                    if(os != null)
                        os.close();
                }catch (Exception e){
                    Log.e(TAG, "Error closing file " + fileName, e);
                }
            }

            if(success){
                Log.i(TAG, "JPEG saved at " + fileName);
                //사진 파일명을 결과 인텐트에 설정한다.

                Intent intent = new Intent();
                intent.putExtra(EXTRA_PHOTO_FILENAME, fileName);

                getActivity().setResult(Activity.RESULT_OK, intent);
            }else{
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }
    };

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_camera, container, false);

        Button takePictureButton = (Button) view.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCamera != null){
                    mCamera.takePicture(mShutterCallback, null, mJpegCallback);
                }
            }
        });

        mSurfaceView = (SurfaceView) view.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        //setType()과 SURFACE_TYPE_PUSH_BUFFERS는 지금은 사용금지된 것들이다,
        //그러나 허니콤 이전 장치에서 카메라 미리 보기를 하려면 필요
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //이 surface를 미리보기 영역으로 사용한다는 것을 카메라에게 알려준다.
                if (mCamera != null) {
                    try {
                        mCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        Log.e(TAG, "Error setting up preview display", e);
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mCamera == null) return;

                //surface의 크기가 변경되었으므로 카메라 미리 보기 크기를 변경한다.
                Camera.Parameters parameters = mCamera.getParameters();
                //허용가능한 미리 보기 크기를 결정할 수 있을 때까지 임시로
                //크기는 우리가 어떤 특정값으로 설정할 수 없으며, 허용할 수 없는 경우 예외 발생
                //Camera.Size s = null;
                Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
                parameters.setPreviewSize(s.width, s.height);
                s = getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
                parameters.setPictureSize(s.width, s.height);
                mCamera.setParameters(parameters);
                try {
                    mCamera.startPreview();
                } catch (Exception e) {
                    Log.e(TAG, "Could not start preview", e);
                    mCamera.release();
                    mCamera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //더 이상 이 surface를 사용할 수 없으므로 미리 보기를 중단한다.
                if (mCamera != null)
                    mCamera.stopPreview();
            }
        });

        //사진찍기 버튼이 눌리면 VISIBLE되게 우선은 INVISIBLE로
        mProgressContainer = view.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //메인쓰레드에서 카메라를 open하기 때문에 경고가 나온다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);  //API 9(진저브레드)에서 소개
        } else {
            mCamera = Camera.open();  //API 8(프로요)까지는 이게 필요
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //release전에 인스턴스가 있는지 확인
        //퍼미션을 요청했더라도 사용이 불가능할 수 있기 때문
        //다른 액티비티가 시용 중일 수도 있고, 가상장치에는 없을 수도 있어서
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    //최대로 지원되는 크기 찾기
    /* 사용 가능한 가장 큰 크기를 알아내는 간단한 알고리즘.
    이보다 더 강력한 기능의 코드를 보려면 안드로이드에서 제공하는
    API 데모의 샘플 앱에 있는 CameraPreview.java를 참조하자. */
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height) {
        Camera.Size bestSize = sizes.get(0);
        int largesArea = bestSize.width * bestSize.height;
        for (Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largesArea) {
                bestSize = s;
                largesArea = area;
            }
        }
        return bestSize;
    }
}
