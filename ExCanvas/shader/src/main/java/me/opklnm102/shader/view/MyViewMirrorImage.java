package me.opklnm102.shader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.view.View;

import me.opklnm102.shader.R;

/*
    ComposeShader를 응용하여 점점 흐려지는 모양의 이미지를 생성하고 원본 이미지와 같이
    출력함으로써 거울에 반사된 듯한 모양을 만들어 낸다.

    셰이더를 조합하는 것은 기술이라기보다 감각의 영역에 속한다. 시행착오를 거쳐 여러조합을 만들어 봐야 한다.
 */
public class MyViewMirrorImage extends View {

    public MyViewMirrorImage(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        canvas.drawColor(Color.BLACK);

        Bitmap bit = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher);

        int width = bit.getWidth();
        int height = bit.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1f);
        Bitmap mirror = Bitmap.createBitmap(bit, 0, 0, width, height, matrix, false);

        canvas.drawBitmap(bit, 0, 0, null);
        ComposeShader comp = new ComposeShader(new BitmapShader(mirror, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT),
                new LinearGradient(0, height, 0, height + height, Color.TRANSPARENT, Color.BLACK, Shader.TileMode.REPEAT)
        ,new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
        paint.setShader(comp);
        canvas.drawRect(0, height, width, height + height, paint);
    }
}
