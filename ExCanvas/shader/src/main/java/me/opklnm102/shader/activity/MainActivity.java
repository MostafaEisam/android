package me.opklnm102.shader.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.opklnm102.shader.view.MyViewMirrorImage;

/*
    Shader
    도형의 내부 표면을 채우는 무늬, 형형색색의 무늬로 도형 내부를 채색할 수 있다.

    Shader  ---- LinearGradient
              -- RadialGradient
              -- SweepGradient
              -- BitmapShader
              -- ComposeShader

    표현하고자 하는 무늬에 맞는 셰이더 객체를 생성한 후 Paint객체에 셰이더를 지정하면이후 도형의 내부를 채울 때 사용된다.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = new MyViewMirrorImage(this);

        setContentView(view);
    }
}
