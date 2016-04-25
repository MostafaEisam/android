package kr.co.mash_up.excanvas.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/*
  비트맵으로부터 캔버스를 생성한 후 캔버스에 그리면 이후부터 비트맵 표면에 그려진다.
  비트맵이 곧 캔버스의 표면이 되며 캔버스의 모든 그리기 메소드를 다 사용할 수 있다.
  이 캔버스에 그려지는 그림은 메모리 내부에만 그려질 뿐 화면에는 보이지 않는다.
  -> 백그라운드에서 미리 그려놓을 수 있는데 이런 용도로 사용되는 비트맵을 off screen bitmap이라고 부른다.

  offscreen bitmap은 메모리를 많이 소비하지만 백그라운드에서 미리 준비해 놓을 수 있어 출력 속도가 빠르고 그리는 과정이 보이지 않아 깔끔
  화면을 갱신하려면 이전 그림을 지우고 다시 그려야하는데 이 작업을 화면에서 수행하면 지워진 모습과 그려진 모습이 교대로 보여 깜박거림 발생
  메모리의 비트맵에 그리면 중간 과정이 보이지 않고 최종완성모습만 보이기 때문에 깜박임이 없다.
  -> 더블 버퍼링 기법
 */
public class MyViewOffScreen extends View{
    public MyViewOffScreen(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.LTGRAY);
        Paint paint = new Paint();

        Bitmap BackBit = Bitmap.createBitmap(300, 200, Bitmap.Config.ARGB_8888);
        Canvas offscreen = new Canvas(BackBit);
        offscreen.drawColor(Color.GREEN);
        paint.setColor(Color.RED);
        for(int x=0; x<300; x+=10){
            offscreen.drawLine(x,0,300-x,200,paint);
        }

        canvas.drawBitmap(BackBit, 10, 10, null);
    }
}
