package me.opklnm102.excamera2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

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
        manager = (CameraManager) getSystemService(CAMERA_SERVICE);

        // 6단계 작업으로 사용자가 카메라 미리보기 화면을 터치하면 사진 이미지를 캡처
        mTextureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, " TextureView clicked");
                takePicture();  //사진을 찍는다.
            }
        });
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
                return;
            }
            manager.openCamera(cameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            //예외는 Camera Device에 정상적인 접근이 어려울 때 발생
            e.printStackTrace();
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
        if(null != mCameraDevice){
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }
    
    protected void startPreview(){
        //미리보기를 시작하기 앞서 Camera Device객체가 정상적으로 생성되었는지
        //동시에 화면은 정상적으로 열려져 있는지 점검
        if(null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize){
            Log.d(TAG, "startPreview fail, return");
            return;
        }
        
        //SurfaceTexture객체를 사용하여 Surface객체를 생성
        SurfaceTexture texture = mTextureView.getSurfaceTexture();
        
        if(null == texture){
            Log.d(TAG, "texture is null, return");
            return;
        }
        
        //TextureView로부터 Surface객체를 생성
        Surface surface = new Surface(texture);
        
        try{
            // 3단계 작업으로 미리보기용 템플릿을 사용하여 CaptureRequest객체를 생성
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
        
        // CaptureRequest객체가 사용하는 미리보기용 Surface객체를 등록
        mPreviewBuilder.addTarget(surface);
        
        try{
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
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    // 5단계 작업으로 CaptureRequest객체를 카메라 시스템에 전달
    protected void updatePreview(){
        if(null == mCameraDevice){
            Log.d(TAG, " updatePreview error, return");
        }

        mPreviewBuilder.set(CaptureRequest.CONTROL_MODE,
                CameraMetadata.CONTROL_MODE_AUTO);

        /*
        HandlerThread를 생성하고 HandlerThread에서 제공하는 루퍼를 이용하여 Handler객체를 반환받아 사용
        Handler와 Thread를 동시에 사용할 수 있다.
         */
        HandlerThread handlerThread = new HandlerThread("CameraPreview");
        handlerThread.start();
        Handler backgroundHandler = new Handler(handlerThread.getLooper());

        try{
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, backgroundHandler);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    // 6단계 작업으로 사용자가 화면을 터치하면 사진 이미지를 캡처
    protected void takePicture(){
        //미리보기와 달리 사진 이미지 캡처는 별도 세션을 생성하여 작업
        //따라서 2단계 작업을 다시 수행
        //작업을 수행하기 전에 이전 단계에서 Camera Device객체가 정상적으로 만들어졌는지 확인해야 한다.
        if(null == mCameraDevice){
            Log.d(TAG, " mCameraDevice is null, return");
            return;
        }

        try{
            //카메라 시스템에서 캡처할 수 있는 사진 이미지 해상도를 얻는다.
            Size[] jpegSizes = null;

            // map변수는 StreamConfigurationMap객체를 가리키는 참조 변수
            // 이미 Camera Device객체로부터 반환받았다.
            if(map != null){
                jpegSizes = map.getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;

            //미리보기 화면과 JPEG 이미지는 서로 다른 해상도를 가질 수 있다.
            //시스템에서 제공할 수 있는 최대 해상도로 이미지를 잡는다.
            if(jpegSizes != null && 0 < jpegSizes.length){
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
                    try{
                        //마지막 캡처한 이미지를 선택
                        image = reader.acquireLatestImage();

                        //JPEG이미지는 단 하나의 프레임만을 제공
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }finally {
                        if(image != null){
                            //각각의 Image객체와 ImageReader 객체를 닫는다.
                            image.close();
                            reader.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException{
                    OutputStream output = null;

                    try{
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    }finally {
                        if(null != output){
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
                    try{
                        //CaptureSession이 정상적으로 생성하였다면 CaptureRequest객체를 전송
                        session.capture(captureBuilder.build(), captureListener, backgroundHandler);
                    }catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, backgroundHandler);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }
}
