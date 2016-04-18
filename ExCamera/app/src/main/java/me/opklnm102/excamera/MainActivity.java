package me.opklnm102.excamera;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

//Camera클래스가 5.0부터 삭제되었기 때문에 붙인다.
@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Camera mCamera;
    private Preview mCameraPreview;
//    private CameraPreview mCameraPreview;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        //화면의 타이틀바를 사용하지 않고 단말기의 전체화면을 미리보기 화면으로 사용
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*
        하나 이상의 카메라가 있다면
        public static int getNumberOfCameras () 를 사용하여 장착된 카메라의 수를 확인

        cam - eraId - 0 ~getNumberOfCameras() - 1 의 범위로 사용하고자 하는 카메라 선택
        public static Camera open ( int cam -eraId)를 사용하여 카메라 객체 생성

        반환하는 CameraInfo클래스의 facing필드를 보고 앞, 뒤 카메라 식별
        public static final int CAMERA_FACING_BACK = 0;
        public static final int CAMERA_FACING_FRONT = 1;
        public static void getCameraInfo(int cameraId, CameraInfo cameraInfo)
        */
//        Camera camera = null;
//        try {
//            //카메라 클래스 내 open()을 사용하여 카메라 객체 생성
//            camera = Camera.open();
//        } catch (RuntimeException e) {
//            //카메라 객체 생성에 문제 발생
//        }

        /*
        줌을 하고자 할 때
        public void setZoom(int value);
        줌 기능 설정. 0~getMaxZoom()이 반환하는 값까지 확대, 축소 가능

        getMaxZoom()
        카메라가 제공하는 확대할 수 있는 최대 줌의 크기 반환

        public boolean isZoomSupported();
        카메라가 줌 기능을 제공하는지 확인

        public int getZoom();
        현재 설정된 줌의 크기 반환

        수정한 속성은 public void setParameters(Camera.Parameters params)를 사용하여 재설정해주어야 한다.
         */
        //Camera.Parameters의 인스턴스 생성
//        Camera.Parameters parameters = camera.getParameters();
//
//        if (parameters.isZoomSupported()) {
//            int current = parameters.getZoom();
//            int i = parameters.getMaxZoom();
//            parameters.setZoom((i > current + 2) ? current + 2 : current);
//        }
//
//        //SurfaceView 객체를 생성하고 카메라와 연결시킨다.
//        mCameraPreview = new CameraPreview(this, camera);
//        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
//        preview.addView(mCameraPreview);

        /*-------------------------------------------------------------------------------*/

        //단말기에 장착된 카메라의 수를 확인
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {  //디폴트로 되어 있는 뒤면 카메라 객체를 생성
                mCamera = Camera.open(0);
            } catch (RuntimeException e) {
                Toast.makeText(mContext, "No camera hardware found", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Camera api가 지원하는 사진 포맷 조사
        Camera.Parameters p = mCamera.getParameters();
        List<Integer> list = p.getSupportedPictureFormats();
        for(Integer item : list){
            Log.d(TAG, " Support format " + item);
        }

        //SuerfaceView 크기를 레이아웃이 제공 가능한 최대 크기로 만든다.
        mCameraPreview = new Preview(this, mCamera);
        mCameraPreview.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        //SurfaceView객체를 FrameLayout 객체 내 하나의 뷰로 만든다.
        ((FrameLayout) findViewById(R.id.camera_preview)).addView(mCameraPreview);
        mCameraPreview.setKeepScreenOn(true);

        //사용자가 화면을 터치하면 그때 사진을 찍는다.
        mCameraPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
            }
        });
    }

    /*
    Camera2로 넘어오면서 거의 모든 스마트폰에 카메라가 장착되면서 삭제됨
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            //카메라가 장착된 단말기
            return true;
        } else {
            //카메라가 장착되어 있지 않은 단말기
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCamera != null){
            mCamera.release();  //카메라 리소스를 모두 해제
            mCamera = null;
        }
    }

    private void refreshGallery(File file){
        //사진 이미지는 검색이 가능하도록 미디어 CP에 넣는다.
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.d(TAG, " onShutter'd");
        }
    };

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //여러 단말기에서 조사해보았지만 카메라 클래스를 포함하여 camera2 api역시
            //5.0이전에는 원시 포맷을 지원하지 않는다.
            Log.d(TAG, " onPictureTaken - raw");
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //일반 스레드를 사용하여 캡처한 이미지를 저장
            new SaveImageTask().execute(data);

            //사진을 찍으면 카메라 작업이 중지. 따라서 다시 실행히야한다.
            mCamera.startPreview();
            Log.d(TAG, " onPictureTaken - jpeg");
        }
    };

    private class SaveImageTask extends AsyncTask<byte[], Void, Void>{

        @Override
        protected Void doInBackground(byte[]... data) {

            FileOutputStream outStream = null;

            // byte배열은 JPEG포맷으로 되어있는 사진을 SD Card에 넣는다.
            try{
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/cametest");
                dir.mkdirs();  //저장을 위해 새로운 디렉터리를 만든다.

                //JPEG 파일명을 날짜를 사용하여 만든다.
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                Log.d(TAG, " onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

                //CP에 사진 이미지를 등록한다.
                refreshGallery(outFile);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
