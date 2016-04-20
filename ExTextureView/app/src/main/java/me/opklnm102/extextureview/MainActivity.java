package me.opklnm102.extextureview;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
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
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private TextureView mTextureView;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //TextureView 클래스의 인스턴스 생성
        mTextureView = new TextureView(this);

        //TextureView의 크기를 전체화면 크기로 만든다.
        mTextureView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mTextureView.setSurfaceTextureListener(this);
        setContentView(mTextureView);

        mTextureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
            }
        });

        Toast.makeText(this, "Touch anywhere on screen to take picture", Toast.LENGTH_SHORT).show();
    }

    //TextureView가 생성되어 View의 사용이 가능할 때 호출. 사용가능한 surfaceTexture, 객체가 사용할수 있는 width, height를 인자로 받는다.
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //TextureView가 생성되어 View가 만들어진다면 Camera 객체 생성
        mCamera = Camera.open();

        /*
        카메라에서 디폴트로 설정된 미리보기의 화면 크기로 만들면 다소 적은 화면이 나타난다.
        원칙전으로 물리적으로 지정된 화면 크기보다 화면 비율에 맞추어 화면크기를 맞추어주는것이 좋다.

        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        mTextureView.setLayoutParams(new FrameLayout.LayoutParams(previewSize.width, previewSize.height, Gravity.CENTER));
        */

        try {
            //TextureView를 카메라의 미리보기 화면으로 사용
            mCamera.setPreviewTexture(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Camera.Parameters 객체를 사용하여 카메라의 환경을 설정
        Camera.Parameters parameters = mCamera.getParameters();

        /*
        미러링 기능은 카메라의 노출과 관련된 기능으로 고급 스마트폰이 아니면 보통 이기능을 지원하지 않는다.
        Camera2 API에서 미러링기능이 정상작동하는 단말기라 하더라도 Camera API는 자체버그가 있어 미러링기능을 지원하지 못하고 예외발생
        따라서 아래와 같이 주석처리
        if(parameters.getMaxNumMeteringAreas() > 0){
            List<Camera.Area> meteringAreas = new ArrayList<>();

            Rect areaRect1 = new Rect(-100, -100, 100, 100);
            //비중을 60%로 설정
            meteringAreas.add(new Camera.Area(areaRect1, 600));
            Rect areaRect2 = new Rect(800, -1000, 1000, -800);
            //나머지 영역의 비중을 40%로 설정
            meteringAreas.add(new Camera.Area(areaRect2, 400));
            parameters.setMeteringAreas(meteringAreas);
        }
         */

        //Camera API가 제공하는 Focus Mode 조사
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            //Auto Focus가 이루어지도록 설정
            parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);
        }

        if (!parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_AUTO)) {
            //플래쉬 모드를 자동으로 만들어준다.
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        }

        //사진 이미지를 최상으로 만든다.
        parameters.setJpegQuality(100);
        if (parameters.isZoomSupported()) {
            int current = parameters.getZoom();
            int i = parameters.getMaxZoom();
            parameters.setZoom((i > current + 2) ? current + 1 : current);
        }

        mCamera.setParameters(parameters);

        //카메라의 미리보기를 작동시킨다.
        mCamera.startPreview();
    }

    //인위적으로 View의 화면 크기를 변화시켰을 때 호출, 변화가 발생된 surfaceTexture, width, height를 인자로
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    //View가 삭제될 때 호출. true반환 -> 더이상 사용할 수 없다. false -> SurfaceTexture객체는 삭제되지 않는다.
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        //TextureView가 종료된다면 카메라 미리보기 기능 역시 종료하고 리소스 역시 해제
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    //updateTexImage()를 사용자가 호출시 호출되는 콜백
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
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
            Log.d(TAG, " onPictureTaken - raw");
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //찍은 사진은 별도 날짜, 시간과 함께 SDcard에 저장
            new SaveImageTask().execute(data);
            camera.startPreview();
        }
    };

    //사진을 CP에 넣는다.
    private void refreshGallery(File file){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Void>{

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            try{
                //SD카드에 사진 저장
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);
                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

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
