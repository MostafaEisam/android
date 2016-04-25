package kr.co.mash_up.excanvas.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import kr.co.mash_up.excanvas.R;

/*
  작도 메소드는 기본 도형을 그리기에 적합하지만 사진 같은 복잡한 모양을 표현하기에는 역부족
  -> 비트맵 사용
 */
public class MyViewDrawBitmap extends View {

    public MyViewDrawBitmap(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.LTGRAY);
        Paint paint = new Paint();

        Resources res = getResources();  //리소스 관리자 얻음

        //res폴더에는 이미지뿐만 아니라 색상, 도형 등 여러가지 형태의 리소스가 포함되어 있으므로
        // Bitmap객체를 바로 읽는 것이 아니라 드로블 루트클래스인 Drawable을 읽어주고
        //비트맵을 읽으면 BitmapDrawable타입의 객체가 리턴
        //BitmapDrawable(리소스에 정의되어 있는 비트맵)은 비트맵을 래핑하는 클래스, 비트맵의 여러가지 속성을 정의
        //타일, 확대, 축소, 정렬등의 작업을 할 수 있다.
        BitmapDrawable bd = (BitmapDrawable) res.getDrawable(R.mipmap.ic_launcher);

        // Bitmap(코드에서 직접사용할 수 있는 비트맵 객체) 얻기
        Bitmap bit = bd.getBitmap();  //출력에 사용할 수 있는 Bitmap 추출

//        Bitmap bit = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);  //위의 3줄을 1줄로 축약

        canvas.drawBitmap(bit, 10, 10, null);
        canvas.drawBitmap(bit, null, new Rect(120, 10, 170, 85), null);  //비트맵 전체 출력

        //비트맵의 src영역을 캔버스의 dst영역에 출력
        //dst영역이 비트맵보다 더 크면 확대되고 더 작으면 축소
        canvas.drawBitmap(bit, new Rect(30, 30, 70, 80), new Rect(180, 10, 180 + 80, 10 + 100), null);
    }
}
