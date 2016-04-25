package me.opklnm102.shader.activity;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import me.opklnm102.shader.R;

/*
    셰이더는 다양한 채움 무늬를 만들지만 런타임에만사용할 수 있다는 면에서 활용범위가 제한적
    셰이더는 Paint객체에 설정되어 캔버스에 직접 출력할 때만 사용되므로 코드로만 생성할 수 있으며 디자인타임에 미리 생성 불가

    셰이더보다 좀 더 일반적인 그리기 객체는 Drawable다
    Drawable은 화면에 출력될 수 있는 모든 것을 표현(이미지, 색상, 비트맵, 셰이더)
    코드, XML로 생성할 수 있어 훨씬 더 범용적이고 활용위가 넓다.

    GradientDrawable - 생성자의 인수로 방향과 색상 배열 전달, 주어진 배경을 가득 채우므로 영역은 따로 지정X
    ColorDrawable과 PaintDrawable - 단색 Drawable 생성하되 PaintDrawable은 모서리를 둥글게 표현할 수 있다.
    ShapeDrawalbe - 모양을 가지는 Drawable을 표현하며 도형이라 생각, Shape객체로 생성하고 생성자에 전달

    solid - 채움색
    stroke - 외곽선
    corners - 모서리 보양
    size - 도형 크기. 논리단위로 지정하되 생략시 채색되는 면적 전체를 가득 채운다.
    padding - 상하좌우의 안쪽 여백
    gradient - 채움무늬
    type - 직선, 원형, 원주 그래디언트를 선택
 */
public class ShapeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);

        Button btn;

        //Gradient Drawable
        btn = (Button) findViewById(R.id.button1);
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.BLUE, Color.GREEN});
        btn.setBackground(g);

        //단색 Drawable
        btn = (Button) findViewById(R.id.button2);
        btn.setBackground(new ColorDrawable(Color.GREEN));

        //특정색으로 채우는 Drawable. 모서리는 X, Y 각각에 대해 값을 지정할 수도 있다.
        btn = (Button) findViewById(R.id.button3);
        PaintDrawable pd = new PaintDrawable(Color.YELLOW);
        pd.setCornerRadius(10.0f);
        btn.setBackground(pd);

        //원모양 Drawable
        btn = (Button) findViewById(R.id.button4);
        ShapeDrawable sd = new ShapeDrawable(new OvalShape());
        sd.getPaint().setShader(new RadialGradient(60, 30, 50, Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP));
        btn.setBackground(sd);

        //둥근 사각형 Drawable
        btn = (Button) findViewById(R.id.button5);
        float[] outR = new float[] {5, 5,30,40,5,5,5,5};
        RectF inRect = new RectF(30, 30, 30, 30);
        float[] inR = new float[]{0,0,20,30,0,0,0,0};
        ShapeDrawable sd2 = new ShapeDrawable(new RoundRectShape(outR, inRect, inR));
        sd2.getPaint().setColor(Color.MAGENTA);
        btn.setBackground(sd2);

        //패스 Drawable
        btn = (Button) findViewById(R.id.button6);
        Path path = new Path();
        path.moveTo(5, 0);
        path.lineTo(0, 7);
        path.lineTo(3, 7);
        path.lineTo(3, 10);
        path.lineTo(7, 10);
        path.lineTo(7, 7);
        path.lineTo(10, 7);
        ShapeDrawable sd3 = new ShapeDrawable(new PathShape(path, 10, 10));
        sd3.getPaint().setShader(new LinearGradient(0,0,0,10,0xff00ff00,0xffff0000, Shader.TileMode.CLAMP));
        btn.setBackground(sd3);
    }
}
