package me.opklnm102.shader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.view.View;

/*
    원형 그래디언트
    중심에서 시작하여 원 바깥쪽으로 색상을 점점 변화시킨다.
    무늬의 모양이 원이므로 주로 원 내부를 채색할 때 사용
    직선 그래디언트와 사용하는 방법은 거의 유사하되 두 점 대신 중심점과 반지름을 지정한다는 정도만 다르다.
 */
public class MyViewRadialGrad extends View {

    public MyViewRadialGrad(Context context) {
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
        paint.setShader(new RadialGradient(80, 80, 70, Color.BLUE, Color.WHITE, Shader.TileMode.CLAMP));
        canvas.drawCircle(80, 80, 70, paint);

        //흰색 파란색
        paint.setShader(new RadialGradient(230, 80, 70, Color.WHITE, Color.BLUE, Shader.TileMode.CLAMP));
        canvas.drawCircle(230, 80, 70, paint);

        //여러가지 색 균등
        paint.setShader(new RadialGradient(80, 230, 70, colors, null, Shader.TileMode.CLAMP));
        canvas.drawCircle(80, 230, 70, paint);

        //여러가지 색 차등
        paint.setShader(new RadialGradient(230, 230, 70, colors, pos, Shader.TileMode.CLAMP));
        canvas.drawCircle(230, 230, 70, paint);
    }
}
