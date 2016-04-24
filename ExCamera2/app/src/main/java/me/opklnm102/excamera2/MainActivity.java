package me.opklnm102.excamera2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_PERMISSION_CAMERA_CODE = 1000;

    Size mPreviewSize;  //미리보기 화면의 크기를 관리.
    TextureView mTextureView;
    CameraDevice mCameraDevice;
    CaptureRequest.Builder mPreviewBuilder;
    CameraCaptureSession mPreviewSession;
    CameraManager manager;
    StreamConfigurationMap map;

    /**
     * 사진 이미지를 캡처할 때 이미지의 방향 역시 카메라의 방향에 맞추어 가로와 세로를 만들어주어야 한다.
     * 단말기의 화면 방향은 세로(Portrait)가 기본이지만 카메라와 사진의 방향은 고전적으로 가로(Landscape)다.
     * 스마트폰의 뒷면에 카메라가 부착되어 있다면 카메라가 바라보는 방향이 정방향.
     * -> 스마트폰의 방향과 서로 반대가 된다. 그차이를 다음과 같이 맞춰 준다.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //전체화면을 카메라 미리보기 화면으로 만든다.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //TextureView 객체를 생성하고 화면에 출력
        mTextureView = new TextureView(this);
        mTextureView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        setContentView(mTextureView);

        // 1단계 적업업에 따라 CaeraManager객체를 생성
        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // 6단계 작업으로 사용자가 카메라 미리보기 화면을 터치하면 사진 이미지를 캡처
        mTextureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, " TextureView clicked");
                takePicture();  //사진을 찍는다.
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "퍼미션 허가", Toast.LENGTH_SHORT).show();
                }
        }
    }

    // 2단계 작업으로 TextureView객체가 정상적으로 화면에 나타나면(이 과정을 오픈이라 부른다)
    // Camera Device객체를 생성
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, " onSurfaceTextureAvailable,  width= " + width + ", height= " + height);

            //정상적으로 화면 TextureView객체가 생성되면 그때 Camera Device객체를 얻는 작업을 수행
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, " onSurfaceTextureSizeChanged");

            //뷰의 너비와 높이가 변경되면 아래 메소드를 호출하여 뷰의 화면 비율을 다시 잡는다.
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            Log.d(TAG, " onSurfaceTextureUpdated");
        }
    };

    // width, height는 TextureView의 방향을 바꿀 때 뷰의 화면 비율을 맞추기 위해 사용
    private void openCamera(int width, int height) {

        try {
            // 단말기에 설치된 카메라 가운데 디폴트 카메라 선택
            String cameraId = manager.getCameraIdList()[0];

            // 카메라가 어떤 특성을 갖고 있는지 알아본다.
            CameraCharacteristics cameraCharacteristics = manager.getCameraCharacteristics(cameraId);
            map = cameraCharacteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            // 미리보기용으로 사용하는 TextureView클래스는 제공 가능한 단말기의 화면 크기를 얻는다.
            // 일반적으로 배열의 1번째 인덱스는 클래스가 제공할 수 있는 최대 크기를 가리킨다.
            assert map != null;
            mPreviewSize = map.getOutputSizes(SurfaceTexture.class)[0];

            //다음처럼 CameraManager객체로부터 Camera Device객체를 생성
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                Toast.makeText(MainActivity.this, "camera permission null", Toast.LENGTH_SHORT).show();

                //퍼미션 요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA_CODE);
                return;
            }

            //단말기의 화면 방향에 따라 화면 크기와 비율을 맞춘다.
            configureTransform(width, height);
            manager.openCamera(cameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            //예외는 Camera Device에 정상적인 접근이 어려울 때 발생
            e.printStackTrace();
        }
    }

    //화면 크기와 화면 비율을 조정
//    private void configureTransform(int width, int height){
//        if(mTextureView == null || mPreviewSize == null){
//            return;
//        }
//
//        //단말기의 방향이 변경되었는지 확인. 0(세로), 1(반시계 방향으로 단말기가 +90도로 회전)
//        int rotation = getWindowManager().getDefaultDisplay().getRotation();
//        Matrix matrix = new Matrix();
//        RectF viewRect = new RectF(0, 0, width, height);
//
//        //사진의 미리보기 화면 비율을 계산
//        float aspect = (float)mPreviewSize.getWidth() / mPreviewSize.getHeight();
//
//        //현대 텍스처뷰 중앙의 위치를 파악. 좌표와 회전은 float타입을 사용
//        float centerX = viewRect.centerX();
//        float centerY = viewRect.centerY();
//
//        if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
//            /*
//            화면 비율에 따라 화면 크기를 조절하기 싫다면 전체 화면으로 한다.
//            DisplayMetrics metrics = getResources().getDisplayMetrics();
//            float x = (float) height / metrics.widthPixels;
//            float y = (float) width / metrics.heightPixels;
//            matrix.postScale(x, y, centerX, centerY);
//             */
//            matrix.postScale(1/aspect, aspect, centerX, centerY);
//
//            //스마트폰의 반시계 방향은 카메라 입장에서 시계 방향이 된다. 반대로 회전 시킨다.
//            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
//        }else if(Surface.ROTATION_180 == rotation){
//            matrix.postRotate(180, centerX, centerY);
//        }
//
//        //행렬을 사용하여 텍스처뷰의 화면을 변경.
//        mTextureView.setTransform(matrix);
//    }

    //뷰의 크기를 화면 비율에 따라 늘린다.
    //매개변수는 텍스처뷰의 너비와 높이
    private void configureTransform(int viewWidth, int viewHeight){
        Activity activity = MainActivity.this;

        //단말기의 화면과 카메라의 미리보기 화면이 정상적으로 생성되었을 때 사용
        if(mTextureView == null || mPreviewSize == null || activity == null){
            return;
        }

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();

        //미리보기로 제공 가능한 화면의 해상도는 다음과 같다.
//        CameraCharacteristics cameraCharacteristics = manager.getCameraCharacteristics(cameraId);
//        StreamConfigurationMap map = cameraCharacteristics.get(
//                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//        mPreviewSize = map.getOutputSizes(SurfaceTexture.class)[0];

        // RectF(float left, float top, float right, float bottom)를 사용하여 직사각형을 얻는다.
        // 단말기가 수평으로 눕혀있다하더라도 카메라에서 사용하는 미리보기 화면은 아직 단말기의 방향이 수직 방향이라는 가정하에 높이를 너비로 사용하고 너비를 높이로 사용
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());

        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();

        //단말기가 세로 방향으로 세워져 있다면 미리보기 방향과 같은 방향이므로 별다른 작업X
        //세로 방향에 대해 화면 비율을 맞추는 것은 별 의미가 없다.
        if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
            /*
            단말기의 방향이 수평 방향일 때 텍스처뷰의 중앙 위치와 미리보기 화면의 중앙 위치를 서로 일치하도록 맞춘다.
            만약 서로 일치하지 않는다면 화면 크기가 다른 형태로 변환된다.
            사각형의 위치를 (dx, dy)만큼 이동
             */
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());

            /*
            미리보기 화면은 단말기가 제공할 수 있는 뷰 크기 가운데 되도록 현재 텍스처뷰의 크기보다 조금 큰 해상도 선택
            물론 해상도가 높으면 화면 크기만큼 시스템 로드를 발생
            현재 텍스처뷰의 크기를 선택한 미리보기 화면 크기로 맞춘다.
            (RectF src, RectF dst, Matrix.ScaleToFit stf) src를 dst에 맞추기위해 src를 축소,확대 한다.
            ScaleToFit은 열거형으로 다음과 같은 상수 제공
            - public static final Matrix.ScaleToFit CENTER
            원래 크기를 유지하면서 직사각형의 중앙을 기준으로 각각(x,y)의 픽셀 크기를 늘리거나 줄여서 맞춘다.
            - public static final Matrix.ScaleToFit START
            직사각형의 왼쪽 위의 꼭지점을 서로 맞춘 상태에서 같은 크기로 픽셀을 늘리거나 줄여서 맞춘다.
            - public static final Matrix.ScaleToFit END
            직사각형의 오른쪽 아래의 꼭지점을 서로 맞춘 상태에서 같은 크기로 픽셀을 늘리거나 줄여서 맞춘다.
            - public static final Matrix.ScaleToFit FILL
            중앙을 기준으로 , x,y축 각각 독립적으로 직사각형이 서로 꽉차도록 크기를 늘리거나 줄여서 맞춘다.
             다른 상수는 X축을 기준으로  Y축을 맞추거나, Y축을 기준으로 X축을 맞추어 src가 dst 내부로 들어가지만
             FILL은 이미지가 이글어지거나 축소되더라도 서로 직사각형을 맞추게 된다.
             */
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);

            //단말기와 미리보기 화면 모두 가로 방향으로 누워있는 상태이므로 화면과 미리보기 화면 간의 높이와 너비의 비율 차이를
            //계산하고 가장 큰 차이가 나는 비율을 선택
            float scale = Math.max((float)viewHeight / mPreviewSize.getHeight(),
                    (float)viewWidth / mPreviewSize.getWidth());

            /*
            제일 큰 차이가 나는 비율에 맞추어 텍스처뷰의 크기를 늘리는 행렬을 만든다.
            이때 세로축이나 가록축이나 화면 비율 차이가 큰 것이 기준이 된다.
            (float Sx, flost Sy, flost px, flost py)
            (px, py)위치를 좌표의 원점으로 인시갛고 원점을 기준으로 x축의 포인터(또는 좌표)는 Sx 비율에 따라 늘리거나 줄인다.
            동시에 y축의 포인터는 Sy 비율에 따라 늘리거나 줄인다.
             */
            matrix.postScale(scale, scale, centerX, centerY);

            /*
            단말기의 머리 방향이 반시계 방향으로 회전하여 수평 방향을 만들면 +90도가 된다.
            역으로 시계방향으로 회전하면 -90도가 된다.
            카메라의 방향은 반대이므로 -90도가 되도록 맞춘다.
            작업의 최종결과는 텍스처뷰는 실제 뷰보다 좀더 커진 상태로 만들어진다.
            (float degrees, float px, float py) - (회전각, 중심x, 줌심y)
            Camera2 API에서 사용하는 라이브러리는 안드로이드 프레임워크와 무관하게 작동. 따라서 인위적으로 회전하도록 만들어주어야한다.
             */
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }else if(rotation == Surface.ROTATION_180){
            //미리보기는 시계방향으로 이동하고 화면 방향은 반시계방향으로 이동하더라도
            //뒤집었을 때는 결과적으로 동일한 방향을 가리키게 된다.
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    //이미지 포맷과 텍스처뷰 클래스에 맞추어 화면 비율과 해상도를 알아보는 메소드
    //클래스와 함께 출력할 수 있는 이미지의 크기는  화면 비율에 따라 사전에 정해져 있다.
    private void setUpCameraOutputs(int width, int height){
        Activity activity = MainActivity.this;

        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);

        try{
            for(String cameraId:manager.getCameraIdList()){
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                //앞면 카메라는 해상도가 떨어지므로 주로 뒷면 카메라를 사용
                if(characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT){
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                //일반적으로 사진 이미지는 포맷이 제공할 수 있는 최대크기를 사용
                //다음 max()를 사용해 제공된 이미지의 크기를 서로 비교하여 최대 크기를 Size객체로 반환하는 기능을 한다.
                Size largest = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());

                ImageReader imageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, /*maxImages*/2);

                /*
                미리보기 화면은 또다른 하나의 동영상이므로 너무 큰 화면을 사용하거나
                너무 큰 프레임-레이트를 사용한다면 시스템에 많은 부담을 주게 된다.
                따라서 가능하다면 적당한 크기로 선택하는 것이 좋다.
                여기서는 캡처하고자 하는 사진 이미지와 동일한 화면 비율을 선택
                 */
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, largest);

                //화면 방향에 따른 뷰를 그릴때 가로와 세로의 크기를 정한다.
                int orientation = getResources().getConfiguration().orientation;
                if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                    //Todo: 책의 코드가 안돌아간다.
                }else{

                }

                return;
            }
        }catch (CameraAccessException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            //단말기에서 기능을 제공하지 않을 때
        }
    }

    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio){
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();

        //화면 비율이 같고 현재 뷰의 화면보다 큰 모두 화면을 모은다
        for(Size option:choices){
            if(option.getHeight() == option.getWidth() * h / w && (option.getWidth() >= width || option.getHeight() >= height)){
                bigEnough.add(option);
            }
        }

        //모은 화면 가운데 가능한 가장 작은 화면의 크기를 선택
        if(bigEnough.size() > 0){
            return Collections.min(bigEnough, new CompareSizesByArea());
        }else {
            Log.d(TAG, " Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    //Collection.min()에서 사용하는 비교클래스
    static class CompareSizesByArea implements Comparator<Size>{

        @Override
        public int compare(Size lhs, Size rhs) {
            //signum()은 음수, 양수, 제로를 식별하는 메소드
            //양수는 +1, 음수는 -1, 제로는 0을 반환
            return Long.signum((long)lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    /*
    Camera Device 상태에 따라 각각 호출되는 콜백 메소드 구현
    정상, 비정상 작동의 경우만 고려
     */
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;

            //미리보기 화면을 실행
            startPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            Log.d(TAG, " onDisconnected");
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Log.d(TAG, " onError");
        }
    };

    //화면이 가려진다면 Camera Device를 닫아 미리보기를 종료
    @Override
    protected void onPause() {
        super.onPause();
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    protected void startPreview() {
        //미리보기를 시작하기 앞서 Camera Device객체가 정상적으로 생성되었는지
        //동시에 화면은 정상적으로 열려져 있는지 점검
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            Log.d(TAG, "startPreview fail, return");
            return;
        }

        //SurfaceTexture객체를 사용하여 Surface객체를 생성
        SurfaceTexture texture = mTextureView.getSurfaceTexture();

        if (null == texture) {
            Log.d(TAG, "texture is null, return");
            return;
        }

        //TextureView로부터 Surface객체를 생성
        Surface surface = new Surface(texture);

        try {
            // 3단계 작업으로 미리보기용 템플릿을 사용하여 CaptureRequest객체를 생성
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // CaptureRequest객체가 사용하는 미리보기용 Surface객체를 등록
        mPreviewBuilder.addTarget(surface);

        try {
            // 4단계 작업으로 CameraCaptureSession객체를 생성
            // 미리보기용이므로 Texture객체가 생성한 하나의 Surface객체만 사용
            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    mPreviewSession = session;

                    //미리보기의 시작을 위해 CaptureRequest객체를 카메라 시스템에 전송
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    //세션을 생성하지 못했을 때 호출
                    Toast.makeText(MainActivity.this, "onConfigureFailed", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // 5단계 작업으로 CaptureRequest객체를 카메라 시스템에 전달
    protected void updatePreview() {
        if (null == mCameraDevice) {
            Log.d(TAG, " updatePreview error, return");
        }

        mPreviewBuilder.set(CaptureRequest.CONTROL_MODE,
                CameraMetadata.CONTROL_MODE_AUTO);

        /*
        HandlerThread를 생성하고 HandlerThread에서 제공하는 루퍼를 이용하여 Handler객체를 반환받아 사용
        Handler와 Thread를 동시에 사용할 수 있다.
         */
        HandlerThread handlerThread = new HandlerThread("CameraPreview");
        handlerThread.start();  //스레드를 실행
        Handler backgroundHandler = new Handler(handlerThread.getLooper());

        try {
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // 6단계 작업으로 사용자가 화면을 터치하면 사진 이미지를 캡처
    protected void takePicture() {
        //미리보기와 달리 사진 이미지 캡처는 별도 세션을 생성하여 작업
        //따라서 2단계 작업을 다시 수행
        //작업을 수행하기 전에 이전 단계에서 Camera Device객체가 정상적으로 만들어졌는지 확인해야 한다.
        if (null == mCameraDevice) {
            Log.d(TAG, " mCameraDevice is null, return");
            return;
        }

        try {
            //카메라 시스템에서 캡처할 수 있는 사진 이미지 해상도를 얻는다.
            Size[] jpegSizes = null;

            // map변수는 StreamConfigurationMap객체를 가리키는 참조 변수
            // 이미 Camera Device객체로부터 반환받았다.
            if (map != null) {
                jpegSizes = map.getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;

            //미리보기 화면과 JPEG 이미지는 서로 다른 해상도를 가질 수 있다.
            //시스템에서 제공할 수 있는 최대 해상도로 이미지를 잡는다.
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }

            //JPEG 이미지 포맷으로 ImageReader객체를 생성
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);

            /*
            이미지를 캡처에 앞서 ImageReader객체와 TextureView객체로부터 Surface객체를 반환 받아 사용
            이미 미리보기가 작동하는 상태라 하더라도 동일한 TextureView객체를 추가 사용하더라도 별 문제가 없다.
            이렇게 하는 이유는 사진 이미지를 캡처하였을 때 미리보기의 종료와 재실행에 의한 화면이 깜박이는 현상을 없애기 위해서다.
             */
            List<Surface> outputSurfaces = new ArrayList<>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(mTextureView.getSurfaceTexture()));

            //사진을 찍기 위해 CaptureRequest.Builder 객체를 생성
            //사진 이미지의 CaptureRequest객체는 TEMPLATE_STILL_CAPTURE 템플릿을 사용
            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());

            //중요한 기능으로 사진 이미지를 캡처하는 순간에 제대로 사진 이미지가 나타나도록
            //오토 포커스와 자동 노출등의 모든 모드를 자동으로 설정
            //이전 카메라 API는 이 기능을 제대로 지원하지 못했다.
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            /*
            미리보기는 스마트폰이 갖고 있는 센서에 맞추어 화면 방향이 변경되지만 사진 이미지는 미리보기와 다르게 기존 설정된 방향에 따라 캡처가 이루어진다.
            그러므로 혼란을 방지하기 위해 캡처할 이미지 역시 카메라 화면 방향에 맞추어 사진의 너비와 높이를 정해주어야 한다.
             */
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            //캡처한 사진 이미지는 외장형 저장소에 저장한다.
            final File file = new File(Environment.getExternalStorageDirectory() + "/DCIM", "pic.jpg");

            //이미지를 캡처할 때 자동으로 호출되는 ImageReader.OnImageAvailableListener인터페이스를 구현
            ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {

                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        //마지막 캡처한 이미지를 선택
                        image = reader.acquireLatestImage();

                        //JPEG이미지는 단 하나의 프레임만을 제공
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            //각각의 Image객체와 ImageReader 객체를 닫는다.
                            image.close();
                            reader.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;

                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };

            //이미지를 캡처하는 작업은 메인스레드가 아닌 스레드핸들러로 수행
            HandlerThread thread = new HandlerThread("CameraPicture");
            thread.start();
            final Handler backgroundHandler = new Handler(thread.getLooper());

            //ImageReader객체와 ImageReader.OnImageAvailableListener객체를 서로
            //연결시켜주기 위해 아래 설정 메소드를 사용
            reader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler);

            // 사진 이미지를 캡처한 이후 호출되는 CameraCaptureSession.CaptureCallback
            // 객체를 생성하여 정상적으로 캡처하였는지 알아본다.
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(MainActivity.this, "Saved: " + file, Toast.LENGTH_SHORT).show();

                    // 이미지를 성공적으로 캡처하였다면 다시 미리보기를 수행
                    startPreview();
                }
            };

            //사진 이미지를 캡처하는데 사용하는 CameraCaptureSession객체를 생성
            //기존에 이미 CameraCaptureSession이 존재한다면, 기존 세션은 자동 종료
            // 그러나 필요에 따라 기존 세션의 빠른 종료를 위해 abortCaptures()를 호출하기도 한다.
            mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        //CaptureSession이 정상적으로 생성하였다면 CaptureRequest객체를 전송
                        session.capture(captureBuilder.build(), captureListener, backgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
