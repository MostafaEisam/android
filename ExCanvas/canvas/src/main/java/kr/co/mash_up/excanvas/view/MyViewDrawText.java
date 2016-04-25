package kr.co.mash_up.excanvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

/*
  Android의 텍스트 출력 기능은 FreeType라는 라이브러리에 의해 지원
  글꼴에 관한 모든 기능을 제공하는 막강한 라이브러리

  글자 좌표의 기준은 왼쪽 하단
 */
public class MyViewDrawText extends View {

    public MyViewDrawText(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.LTGRAY);
        Paint paint = new Paint();
        String str = "Made in Korea";
        char[] arCh = {'a', 'b', 'c', 'd'};

        //기본 문자열 출력, 안티알리아싱 적용
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        canvas.drawText(str, 10 ,20, paint);
        canvas.drawText(str, 2 ,11, 10, 40, paint);
        canvas.drawText(arCh, 0, 3, 10, 60, paint);

        //수평 정렬
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Left Align", 100, 90, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Center Align", 100, 110, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Right Align", 100, 130, paint);

        //수직 정렬은 항상 글꼴의 베이스에 맞춰진다.
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.RED);
        canvas.drawText("Small", 10, 180, paint);
        paint.setTextSize(20);
        canvas.drawText("Mid", 40, 180, paint);
        paint.setTextSize(30);
        canvas.drawText("Big", 80, 180, paint);
        paint.setTextSize(40);
        canvas.drawText("Hy", 125, 180, paint);

        //여러가지 속성 동시에 적용해 보기
        paint.setColor(Color.BLUE);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
        paint.setUnderlineText(true);
        paint.setStrikeThruText(true);
        paint.setSubpixelText(true);
        paint.setTextSkewX(-0.25f);
        paint.setTextScaleX(0.8f);
        canvas.drawText(str, 10, 220, paint);
    }
}
