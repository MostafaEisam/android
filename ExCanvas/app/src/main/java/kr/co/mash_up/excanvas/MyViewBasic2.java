package kr.co.mash_up.excanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by Dong on 2016-04-25.
 */
public class MyViewBasic2 extends View {

    public MyViewBasic2(Context context) {
        super(context);
    }

    /*
       각 색상 요소별로 강도를 지정하거나 16진수로 한꺼번에 색상을 표현
       void drawRGB(int r, int g, int b)
       void drawARGB(int a, int r, int g, int b)
       void drawColor(int color)
       void drawPaint(Paint paint) - 단색뿐만 아니라 이미지, 그라데이션 등을 채울 수 있다.

       복잡한 도형 그리기
       void drawRoundRect(RectF rect, float rx, float ry, Paint paint) - 모서리가 둥근 사각형. 둥근 정도를 rx, ry로 지정. 값이 클수록 둥글어진다.
       void drawOval(RectF oval, Paint paint) - 지정한 사각형에 내접한 원을 그린다.
       void drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint) - startAngle부터 sweepAngle만큼 원호를 그린다.
       useCenter는 원호의 끝과 중심을 연결할지 결정, true면 피자조각, false면 반달 모양
       void drawLines(float[] pts, Paint paint) - 지정된 선을 순서대로 그린다.
       void drawPoints(float[] pts, int offset, int count, Paint paint) - 여러점 찍기
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawARGB(255, 204, 204, 204);
//        canvas.drawRGB(204, 204, 204);
//        canvas.drawColor(0xffcccccc);
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        canvas.drawPaint(paint);

        //빨간색 둥근 사각형
        RectF r = new RectF(10, 10, 100, 100);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(r, 10, 10, paint);

        //타원
        r.set(110, 10, 150, 100);
        canvas.drawOval(r, paint);

        //분홍색 반달
        paint.setColor(Color.MAGENTA);
        r.set(10, 110, 100, 200);
        canvas.drawArc(r, 10, 150, false, paint);

        //분홍색 파이
        r.set(110, 110, 200, 200);
        canvas.drawArc(r, 10, 150, true, paint);

        //파란색 다각선
        paint.setColor(Color.BLUE);
        float[] pts = {10, 210, 50, 250, 50, 250, 110, 220};
        canvas.drawLines(pts, paint);

        //검은색 점 3개
        paint.setColor(Color.BLACK);
        float[] pts2 = {20, 210, 50, 240, 100, 220};
        canvas.drawPoints(pts2, paint);
    }
}
