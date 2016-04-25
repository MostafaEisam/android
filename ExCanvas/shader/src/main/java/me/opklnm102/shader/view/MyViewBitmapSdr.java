package me.opklnm102.shader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

import me.opklnm102.shader.R;

/*
    BitmapShader는 비트맵을 무늬로 사용
    비트맵은 전문적인 그래픽 툴로 제작 가능
    표현력이 가장 섬세, 미리 그려진 그림을 고속으로 복사하므로 출력 속도도 빠르다.
    복잡한 패턴을 반복적으로 칠할 때 주로 사용

    타일모드는 사로, 세로 방향으로 각각 따로 지정할 수 있다.
    비트맵보다 채색 영역이 더 넓은 경우가 대부분이므로 CLAMP모드는 거의 사용되지 않으며 REPEAT나 MIRROR모드를 주로 사용
    채색에 사용할 비트맵은 종이, 대리석처럼 반복해도 경계가 눈에 띄지 않는것이 보기에 좋다.
 */
public class MyViewBitmapSdr extends View {

    public MyViewBitmapSdr(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Bitmap clover = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher);

        paint.setShader(new BitmapShader(clover, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        canvas.drawRect(0, 0, 320, 150, paint);
        paint.setShader(new BitmapShader(clover, Shader.TileMode.MIRROR, Shader.TileMode.REPEAT));
        canvas.drawRect(0, 160, 320, 310, paint);
    }
}
