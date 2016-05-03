package me.opklnm102.exdraw;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

/*
  필터
  이미지를 구성하는 각 픽셀을 수학적인 연산으로 조작함으로써 출력 결과를 변형하는 것
  픽셀하나는 ARGB요소로 구성, 각요소는 강도를 표현. 일정한 규칙에 따라 이값을 조정하면 이미지 출력 결과가 바뀐다.

  적용되는 요소에 따라
  1. 마스크 필터
  2. 색상 필터
  로 구분(세부적으로는 5개로 나눈다)

  Paint에
  MaskFilter setMaskFilter(MaskFilter maskfilter)
  ColorFilter setColorFilter(ColorFilter filter)
  필터를 제거하려면 인수로 null 제공

  MaskFilter로부터 파생된 2개의 필터
  BlurMaskFilter(folat radius, BlurMaskFilter.Blur style)
  EmbossMaskFilter(float[] direction, float ambient, float specular, float blurRadius)
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        setContentView(new BlurFilterView(this));

    }


}
