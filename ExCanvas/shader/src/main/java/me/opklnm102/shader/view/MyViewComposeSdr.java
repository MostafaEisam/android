package me.opklnm102.shader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.view.View;

import me.opklnm102.shader.R;

/*
    2개의 셰이더를 결합하여 같이 적용
    단일 셰이더의 표현력은 한계가 있지만 둘이상의 셰이더를 결합하면 훨씬 더 세련된 무늬를 만들 수 있다.
 */
public class MyViewComposeSdr extends View {

    public MyViewComposeSdr(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Bitmap clover = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher);

        ComposeShader comp = new ComposeShader(new BitmapShader(clover, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT),
                new LinearGradient(0, 0, 320, 0, 0x0, Color.BLACK, Shader.TileMode.REPEAT),
                new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
        paint.setShader(comp);
        canvas.drawRect(0, 0, 320, 200, paint);

    }
}
