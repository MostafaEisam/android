package me.opklnm102.wallpaper;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

/*
  대부분의 설정은 Settings클래스에 포함
  일부 설정은 별도의 조정 방법이 따로 제공
  ex) 볼륨조정 - AudioManager. GPS설정 - 맵서비스 API, 배경벽지 - WallpaperManager

   WallpaperManager mWallpaperManager = WallpaperManager.getInstance(this);

   현재 설정된 벽지를구할 떄
     Drawable getDrawable() - 현재 설정된 벽지를 조사하되 벽지가 없을 경우 시스템의 디폴트 벽지 리턴
                              ImageView에 바로 쓸수있도록 Drawable객체 리턴
     Drawable getFastDrawable() - 배경이미지는 용량이 커 조사 시간이 오래걸리는데. 스케일링, 알파 조정,
                                  색상 필터 등의 몇가지 특성을 무시하고 빠른 속도로 배경벽지 조사
     WallpaperInfo getWallpaperInfo() - 라이브 벽지의 정보를 조사. 단순이미지인 경우 null 리턴

   벽지를 변경할 때
     void setBitmap(Bitmap bitmap)
     void setResource(int resid)
     void setStream(InputStream data)
     코드에서 내부적으로 생성한 비트맵이나 디자인 타임에 미리 저장해놓은리소스 등을 지정. 파일스트림으로도 가능
     지정한 비트맵은 png로 변환되어 저장
     벽지 변경 후 ACTION_WALLPAPER_CHANGED 방송
     퍼미션 필요 - <uses-permission android:name="android.permission.SET_WALLPAPER"/>
     벽지는 폰의 외향을 결정하는 중요한 요소여서 사용자가 자주 변경하는 설정 중 하나

     Android가 심플한 단색, 단순 패턴등의 벽지를 지원하지 않아서 만든 샘플App
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivPreview;
    WallpaperManager mWallpaperManager;
    Spinner spinnerColor1, spinnerColor2, spinnerDirection;
    RadioGroup rgType;
    RadioButton rbType1, rbType2;
    Button btnSolid;


    public static final int WIDTH = 120;
    public static final int HEIGHT = 100;
    int[] arColor = new int[]{0xff000000, 0xffffffff, 0xff404040, 0xff808080,
            0xffc0c0c0, 0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffff00, 0xff00ffff,
            0xffff00ff, 0xff008000, 0xff000080};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();

        ArrayAdapter<CharSequence> adColor = ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_dropdown_item);
        adColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor1.setPrompt("첫 번째 색상을 고르세요");
        spinnerColor1.setAdapter(adColor);
        spinnerColor1.setOnItemSelectedListener(mItemSelectedListener);

        spinnerColor2.setPrompt("두 번째 색상을 고르세요");
        spinnerColor2.setAdapter(adColor);
        spinnerColor2.setOnItemSelectedListener(mItemSelectedListener);
        spinnerColor2.setSelection(1);

        ArrayAdapter<CharSequence> adDir = ArrayAdapter.createFromResource(this, R.array.gradientdir, android.R.layout.simple_spinner_item);
        adDir.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDirection.setPrompt("방향을 고르세요.");
        spinnerDirection.setAdapter(adDir);
        spinnerDirection.setOnItemSelectedListener(mItemSelectedListener);

        mWallpaperManager = WallpaperManager.getInstance(this);
        ivPreview.postDelayed(new Runnable() {
            @Override
            public void run() {
                Drawable wallpaperDrawable = mWallpaperManager.getDrawable();
                ivPreview.setImageDrawable(wallpaperDrawable);
            }
        }, 500);

        rbType1.setOnClickListener(this);
        rbType2.setOnClickListener(this);
        btnSolid.setOnClickListener(this);

        spinnerColor2.setEnabled(false);
        spinnerDirection.setEnabled(false);
    }

    AdapterView.OnItemSelectedListener mItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            updatePreview();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void updatePreview() {
        if (rgType.getCheckedRadioButtonId() == R.id.radioButton_type1) {
            ivPreview.setImageBitmap(makeSolidBitmap(arColor[spinnerColor1.getSelectedItemPosition()]));
        } else {
            ivPreview.setImageBitmap(makeGradientBitmap(arColor[spinnerColor1.getSelectedItemPosition()],
                    arColor[spinnerColor2.getSelectedItemPosition()],
                    spinnerDirection.getSelectedItemPosition()));
        }
    }

    public void setWallpaperBitmap(Bitmap bitmap) {
        try {
            mWallpaperManager.setBitmap(bitmap);  //벽지로 등록
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Bitmap makeSolidBitmap(int color) {
        Bitmap backBit = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas offScreen = new Canvas(backBit);
        offScreen.drawColor(color);
        return backBit;
    }

    Bitmap makeGradientBitmap(int color1, int color2, int dir) {
        Bitmap backBit;
        Canvas offScreen;
        Paint paint;
        backBit = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        offScreen = new Canvas(backBit);
        paint = new Paint();
        paint.setAntiAlias(true);
        switch (dir) {
            case 0:
                paint.setShader(new LinearGradient(0, 0, 0, HEIGHT, color1, color2, Shader.TileMode.CLAMP));
                break;
            case 1:
                paint.setShader(new LinearGradient(0, 0, WIDTH, 0, color1, color2, Shader.TileMode.CLAMP));
                break;
            case 2:
                paint.setShader(new LinearGradient(0, HEIGHT, WIDTH, 0, color1, color2, Shader.TileMode.CLAMP));
                break;
            case 3:
                paint.setShader(new LinearGradient(0, 0, WIDTH, HEIGHT, color1, color2, Shader.TileMode.CLAMP));
                break;
        }
        offScreen.drawRect(0, 0, WIDTH, HEIGHT, paint);
        return backBit;
    }

    void initView() {
        ivPreview = (ImageView) findViewById(R.id.imageView_preview);
        spinnerColor1 = (Spinner) findViewById(R.id.spinner_color1);
        spinnerColor2 = (Spinner) findViewById(R.id.spinner_color2);
        spinnerDirection = (Spinner) findViewById(R.id.spinner_direction);
        rgType = (RadioGroup) findViewById(R.id.radioGroup_type);
        rbType1 = (RadioButton) findViewById(R.id.radioButton_type1);
        rbType2 = (RadioButton) findViewById(R.id.radioButton_type2);
        btnSolid = (Button) findViewById(R.id.button_solid);
    }

    @Override
    public void onClick(View v) {
        int color1, color2;
        int dir;

        switch (v.getId()) {
            case R.id.button_solid:
                if (rgType.getCheckedRadioButtonId() == R.id.radioButton_type1) {
                    color1 = spinnerColor1.getSelectedItemPosition();
                    setWallpaperBitmap(makeSolidBitmap(arColor[color1]));
                } else {
                    color1 = spinnerColor1.getSelectedItemPosition();
                    color2 = spinnerColor2.getSelectedItemPosition();
                    dir = spinnerDirection.getSelectedItemPosition();
                    setWallpaperBitmap(makeGradientBitmap(arColor[color1], arColor[color2], dir));
                }
                Toast.makeText(MainActivity.this, "벽지를 변경하였습니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButton_type1:
                spinnerColor2.setEnabled(false);
                spinnerDirection.setEnabled(false);
                updatePreview();
                break;
            case R.id.radioButton_type2:
                spinnerColor2.setEnabled(true);
                spinnerDirection.setEnabled(true);
                updatePreview();
                break;
        }
    }
}
