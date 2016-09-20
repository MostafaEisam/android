package me.dong.exviewcapture;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * http://androidhuman.com/441
 */
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 100;

    LinearLayout llRoot;

    Button btnCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llRoot = (LinearLayout) findViewById(R.id.ll_root);
        btnCapture = (Button) findViewById(R.id.button_capture);
        btnCapture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    viewCapture();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Phone State Permission")
                                .setMessage("permission...")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestWriteStoragePermission();
                                    }
                                })
                                .create()
                                .show();
                    } else {
                        requestWriteStoragePermission();
                    }
                }
            }
        });
    }

    private void requestWriteStoragePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "사용자가 퍼미션 허가함", Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        viewCapture();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "사용자가 퍼미션 거부함", Toast.LENGTH_SHORT).show();
                    //Todo: 다른 대첵을 찾거나 에러처리
                }
        }
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void viewCapture() {
        //Drawing cache enabled속성이 true면 (뷰 업데이트마다 drawing cache에 저장)
        llRoot.buildDrawingCache();  //drawing cache에 저장
        Bitmap captureView = llRoot.getDrawingCache();  //drawing cache에 저장된 뷰 이미지 얻기
        FileOutputStream fos;
        try {
            File dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            fos = new FileOutputStream(dirPath.toString() + "/capture_" + System.currentTimeMillis() + ".jpg"); //스트림 열기
            captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos);  //이미지 저장
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.this, "Captured!", Toast.LENGTH_SHORT).show();
    }
}
