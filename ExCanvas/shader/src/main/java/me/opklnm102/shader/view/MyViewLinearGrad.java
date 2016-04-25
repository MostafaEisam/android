package me.opklnm102.shader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

/*
   직선 그래디언트
   두가지 or 몇가지 색상을 지정해두고 두 좌표 사이를 일직선으로 이동하면서 색상이 점점 변하는 무늬를 정의

   TileMode.CLAMP - 무늬의 끝부분을 계속 반복
   TileMode.REPEAT - 무늬를 반사시켜 계속 반복
   TileMode.MIRROR - 똑같은 무늬를 계속 반복

    public LinearGradient(float x0, float y0, float x1, float y1, int color0, int color1, TileMode tile)
    2가지 색상 사용. (x0, y0)에 color0색상을 찍고 (x1,y1)쪽으로 이동하면서 color1색상을 찍으며 그사이에는 두점과
    상대적인 거리에 따라 색상을 적당히 섞어서 채색

    public LinearGradient(float x0, float y0, float x1, float y1, int colors[], float positions[], TileMode tile)
    colors배열로 여러가지 색상을 지정. position(0~1 사이의 값)배열로 색상이 나타날 상대적인 위치 지정. 배열의 크기는 같아야 한다.
    position이 null이면 모든 색상이 균등한 폭을 가진다.
 */
public class MyViewLinearGrad extends View {
    public MyViewLinearGrad(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.WHITE};
        float[] pos = {0.0f, 0.1f, 0.6f, 0.9f, 1.0f};

        //수평
        paint.setShader(new LinearGradient(0, 0, 100, 0, Color.BLUE, Color.WHITE, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, 100, 100, paint);

        //우하향
        paint.setShader(new LinearGradient(110, 0, 210, 100, Color.BLUE, Color.WHITE, Shader.TileMode.CLAMP));
        canvas.drawRect(110, 0, 210, 100, paint);

        //우상향
        paint.setShader(new LinearGradient(220, 100, 320, 0, Color.BLUE, Color.WHITE, Shader.TileMode.CLAMP));
        canvas.drawRect(220, 0, 320, 100, paint);

        //가장자리 반복
        paint.setShader(new LinearGradient(0, 0, 100, 0, Color.BLUE, Color.WHITE, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 110, 320, 150, paint);

        //무늬 반복
        paint.setShader(new LinearGradient(0, 0, 100, 0, Color.BLUE, Color.WHITE, Shader.TileMode.REPEAT));
        canvas.drawRect(0, 160, 320, 200, paint);

        //무늬 반사 반복
        paint.setShader(new LinearGradient(0, 0, 100, 0, Color.BLUE, Color.WHITE, Shader.TileMode.MIRROR));
        canvas.drawRect(0, 210, 320, 250, paint);

        //여러가지 색상 균등 배치
        paint.setShader(new LinearGradient(0, 0, 320, 0, colors, null, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 260, 320, 300, paint);

        //여러가지 색상 임의 배치
        paint.setShader(new LinearGradient(0, 0, 320, 0, colors, pos, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 310, 320, 350, paint);
    }
}
