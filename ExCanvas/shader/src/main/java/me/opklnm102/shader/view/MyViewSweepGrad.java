package me.opklnm102.shader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.view.View;

/*
    원주의 3시 방향에서 시작하여 시계방향으로 회전하면서 색상이 변한다.
    채색영역의 끝까지 무늬가 확장되므로 중심점만 지정하면 반지름은 따로 지정하지 않는다.
    무늬가 어디까지 확장될 것인가는 그리는 도형의 크기에 의해 결정
 */
public class MyViewSweepGrad extends View {

    public MyViewSweepGrad(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.WHITE };
        float[] pos = {0.0f, 0.1f, 0.6f, 0.9f, 1.0f };

        //파란색 흰색
        paint.setShader(new SweepGradient(80, 80, Color.BLUE, Color.WHITE));
        canvas.drawCircle(80, 80, 70, paint);

        //흰색 파란색
        paint.setShader(new SweepGradient(230, 80, Color.WHITE, Color.BLUE));
        canvas.drawCircle(230, 80, 70, paint);

        //여러가지 색 균등
        paint.setShader(new SweepGradient(80, 230, colors, null));
        canvas.drawCircle(80, 230, 70, paint);

        //여러가지 색 차등
        paint.setShader(new SweepGradient(230, 230, colors, pos));
        canvas.drawCircle(230, 230, 70, paint);
    }
}
