package me.opklnm102.exdraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/*
   BlurFilter - MaskFilter로부터 파생된 필터
                가장자리 부분의 색상을 흐릿하게 만든다.(뽀샤시 효과)
                이미지 아래쪽에 흐릿한 그림자, 이미지의 가장자리를 부드럽게 만드는 효과
                생성자로 흐릿하게 보일 영역의 반지름과 스타일을 지정

                BlurMaskFilter(folat radius, BlurMaskFilter.Blur style)
                radius - 클수록 영향을 받는 영역이 넓어진다.
                style - 필터가 적용될 영역이나 방식을 지정(INNER, NORMAL, OUTER, SOLID)
 */

//이미지의 가장자리 20픽셀만큼을 흐리게 출력
public class BlurFilterView extends View {

    public BlurFilterView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cheese);
        BlurMaskFilter blur = new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL);
        paint.setMaskFilter(blur);
        canvas.drawBitmap(bitmap, 30, 30, paint);
    }
}
