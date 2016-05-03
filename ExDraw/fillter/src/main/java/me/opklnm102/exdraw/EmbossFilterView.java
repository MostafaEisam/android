package me.opklnm102.exdraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.view.View;

/*
    EmbossFilter - 올록볼록 화장지에 적용되는 효과를 적용하여 경계부근이 솟아난것처럼 출력하여 입체감 부여
    EmbossMaskFilter(float[] direction, float ambient, float specular, float blurRadius)
    direction - 방향
    ambient - 주변 빛의 양
    specular - 블러링 효과강도
    blurRadius -

 */
public class EmbossFilterView extends View {

    public EmbossFilterView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cheese);
        EmbossMaskFilter emboss = new EmbossMaskFilter(new float[]{2, 2, 2}, 0.5f, 6, 5);
        paint.setMaskFilter(emboss);
        canvas.drawBitmap(bitmap, 30, 30, paint);
    }
}
