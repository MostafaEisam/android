package kr.co.mash_up.excanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Dong on 2016-04-25.
 */
public class MyViewPaintStyle extends View {

    Paint mPaint;  //onDraw()는 그릴 때마다 호출되므로 Paint객체는 생성자등에서 미리 생성하는게 좋다.

    public MyViewPaintStyle(Context context) {
        super(context);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(8);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.LTGRAY);

        //채우기
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(50, 50, 40, mPaint);

        //외곽선 그리기
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(150, 50, 40, mPaint);

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);  //FILL과 비교해 크기가 다르다. 획의 두께만큼 원이 확장
        canvas.drawCircle(250, 50, 40, mPaint);

        //파란색으로 채우고 빨간색으로 외곽선 그리기
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(50, 150, 40, mPaint);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(50, 150, 40, mPaint);

        //빨간색으로 외곽선 그리고 파란색으로 채우기
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(150, 150, 40, mPaint);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(150, 150, 40, mPaint);
    }
}
