package me.opklnm102.excamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by opklnm102 on 2016-04-18.
 * 카메라, 비디오의 재생, 오디오 재생 작업 모두의 공통점은 "메인 스레드가 아닌 일반 스레드로 실행해야 한다."는 특징을 갖는다.
   SurfaceView클래스 사용

   하면에 문자나 이미지를 출력하는 원리는 "app이 안드로이드에서 제공하는 가상화면에 그림을 그리면, SurfaceManager가 일괄적으로 실제화면에 완성된 그림을 출력"

   SurfaceManager의 핵심 기능은 SurfaceFlinger라고 하는 C++언어로 작성된 프로그램이 담당
   SurfaceFlinger는 화면에 뷰를 출력하는 기능이외에 메시지큐를 관리하는 기능을 수행

   SurfaceView는 일반 스레드로 동작, Thread-UnSafe하기 때문에 메인 스레드에서 동작하는 SurfaceFlonger라는 프레임워크와 어떤 방식이던 상호연동해야 한다.

   뷰를 출력시키려면일정 시점에 화면이라고 하는 리소스를 점유해야 한다. 이를 위해 제공되는 것이 SurfaceHolder인터페이스

   SurfaceHolder는 SurfaceFlinger로부터 화면에 작업할 수 있는 동기화 시점을 알려주는 기능 수행
   작업은 SurfaceHolder인터페이스의 SurfaceHolder.Callback라고 하는 내부 인터페이스의 콜백 메소드를 통해 이루어 진다.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    public static final String TAG = CameraPreview.class.getSimpleName();

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        //SurfaceView객체 생성과 함께 Holder객체를 생성하고 Callback을 연결
        mSurfaceHolder = getHolder();

        //여러개의 callback 설정가능. 순차적으로 작업 수행
        mSurfaceHolder.addCallback(this);
    }

    //안드로이드 프레임워크 가운데 하나인 서비스매니저가 SurfaceView객체를
    //사용하면 화면 내 서피스뷰를 정상적으로 생성하였다면 호출
    //카메라와 SurfaceHolder객체를 연결해주고 미리보기 시작
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }catch (IOException e){
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    //화면 방향이 변경되거나 다른 문제로 화면크기가 변경되었을 때 호출.(뷰의 이동, 크기의 변화)
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        //카메라의 미리보기 작업을 중단하고 다시 뷰를 호출
        if(mSurfaceHolder.getSurface() == null){
            //미리보기용 화면이 정상적으로 만들어지지 않았다면 반환
            return;
        }

        //surfaceChanged()가 호출되면서 기존의 미리보기를 중단
        try {
            mCamera.stopPreview();
        }catch (Exception e){
            return;  //예외가 발생하면 작업을 중단하고 반환
        }

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        }catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    //surface를 삭제시키기전에 호출
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //화면이 닫아지면 카메라 미리보기를 중단
        if(mCamera != null){
            mCamera.stopPreview();
        }
    }
}
