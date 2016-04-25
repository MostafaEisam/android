package kr.co.mash_up.excanvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by Dong on 2016-04-25.
 */
public class MyViewStroke extends View {

    public MyViewStroke(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.LTGRAY);
        Paint paint = new Paint();

        //캡 모양 테스트. 선분의 끝 모양 결정
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(16);  //
        canvas.drawLine(50, 30, 240, 30, paint);  //BUTT(default). 지정한 좌표에서 선이 끝난다.
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(50, 60, 240, 60, paint);  //둥근 모양으로 끝이 장식
        paint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawLine(50, 90, 240, 90, paint);  //사각형 모양이되 지정한 좌표보다 조금 더 그어진다.

        //조인 모양 테스트. 선분이 만나 각지는 곳의 모양 결정
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);  //90도로 각진 형태를 그린다(default)
        canvas.drawRect(50, 150, 90, 200, paint);
        paint.setStrokeJoin(Paint.Join.BEVEL);  //깍은 모양으로 그린다.
        canvas.drawRect(120, 150, 160, 200, paint);
        paint.setStrokeJoin(Paint.Join.ROUND);  //둥근 모양으로 그린다.
        canvas.drawRect(190, 150, 230, 200, paint);
    }
}
