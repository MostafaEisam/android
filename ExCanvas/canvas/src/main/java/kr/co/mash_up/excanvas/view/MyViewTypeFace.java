package kr.co.mash_up.excanvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

/*
    글자의 모양을 결정하는 가장 중요한 정보는 타입페이스
    타입페이스 - 바탕, 굴림 등 글꼴에 대한 고유의 이름
 */
public class MyViewTypeFace extends View {

    public MyViewTypeFace(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.LTGRAY);
        Paint paint = new Paint();
        int y = 1;
        paint.setAntiAlias(true);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Font:Default", 10, y++ * 40, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.NORMAL));
        canvas.drawText("Font:Default Bold", 10, y++ * 40, paint);
        paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
        canvas.drawText("Font:Monospace", 10, y++ * 40, paint);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        canvas.drawText("Font:Sans Serif", 10, y++ * 40, paint);
        paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
        canvas.drawText("Font:Serif", 10, y++ * 40, paint);
    }
}
