package me.dong.exgallery.local;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;

import me.dong.exgallery.R;

public class GalleryActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
        tryOutStorage();
    }

    public void initFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_fragment_gallery_image, fragment, GalleryFragment.TAG)
                .commitAllowingStateLoss();
    }

    final int WRITE_EXTERNAL_STORAGE_CODE = 0;

    public void tryOutStorage() {
        /*
         ContextCompat.checkSelfPermission(Context context, String permission)
         (액티비티, 조사할 퍼미션 이름)
         퍼미션을 가지고 있으면 PackageManager.PERMISSION_GRANTED 리턴
         없으면 PackageManager.PERMISSION_DENIED
         */
        if (ContextCompat.checkSelfPermission(GalleryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(GalleryActivity.this, "허가된 상태", Toast.LENGTH_SHORT).show();
            GalleryFragment galleryFragment = GalleryFragment.newInstance();
            initFragment(galleryFragment);
        } else {
            /*
            사용자에게 퍼미션에 대해 설명
            최소 1번정도 거절했을 때 도움말 출력
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)는 모든 상황을보고 설명이 필요한 시점이면 true 반환
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(GalleryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(GalleryActivity.this)
                        .setMessage("이 프로그램이 원활하게 동작하기 위해서는 " +
                                "주소록 읽기 퍼미션이 꼭 필요합니다." +
                                "퍼미션을 허가해 주세요.")
                        .setTitle("제발~~")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(GalleryActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            } else {
                Toast.makeText(GalleryActivity.this, "허가된 상태가 아니어서 퍼미션 요청", Toast.LENGTH_SHORT).show();

                 /*
            ActivityCompat.requestPermissions(Activity activity, String[] permissions, int requestCode)
            필요한 퍼미션을 한꺼번에 요청하는데 2번째 인수로 머시션의 목록을 배열로 전달
            요청할 퍼미션은 매니페스트에도 반드시 기록되어 있어야 한다.
             */
                ActivityCompat.requestPermissions(GalleryActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
            }
        }
    }

    /*
    (요청코드, 퍼미션 목록, 허가 여부)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE:
                //사용자가 무시하거나 강제로 닫으면 빈배열 전달. null이 아니지만 길이가 0일 수 있으므로 반드시 점검.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(GalleryActivity.this, "사용자가 퍼미션 허가함", Toast.LENGTH_SHORT).show();

                    GalleryFragment galleryFragment = GalleryFragment.newInstance();
                    initFragment(galleryFragment);

                } else {
                    Toast.makeText(GalleryActivity.this, "사용자가 퍼미션 거부함", Toast.LENGTH_SHORT).show();
                    //Todo: 다른 대첵을 찾거나 에러처리
                    this.finish();
                }
        }
    }
}
