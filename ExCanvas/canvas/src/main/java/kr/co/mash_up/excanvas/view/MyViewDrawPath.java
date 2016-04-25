package kr.co.mash_up.excanvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/*
  Path는 직선, 곡선, 다각형 등의 도형 궤적 정보를 가지는 그래픽 객체
  도형의 좌표 정보만 가지므로 그 자체는 화면에 보이지 않으며 캔버스의 drawPath()를 호출해야 화면에 그려진다.
  복잡한 도형을 한꺼번에 그릴 때 그리기 메소드를 일일이 호출하는 것보다 패스를 구성한 후 그리는 것이 효율적
  Path는 1번에 그려지므로 선의 속성이나 모양이 일관되게 적용되는 이점이 있고 여러번 재사용할 수 있다.

  곡선, 백터그래픽 정보를 파일로 저장, 네트워크로 전달, 클리핑 등에 활용
 */
public class MyViewDrawPath extends View {

    public MyViewDrawPath(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path path = new Path();

        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        //원, 사각형을 패스로 정의한 후 출력
        path.addCircle(50, 50, 40, Path.Direction.CW);
        path.addRect(100, 10, 150, 90, Path.Direction.CW);
        canvas.drawPath(path, paint);

        //직선 곡선을 패스로 정의한 후 출력
        path.reset();
        path.moveTo(10, 110);
        path.lineTo(50, 150);
        path.rLineTo(50, -30);  //현재위치를 기준으로 상대위치 사용
        path.quadTo(120, 170, 200, 110);
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLUE);
        canvas.drawPath(path, paint);

        //곡선 패스 출력
        path.reset();
        path.moveTo(10, 220);
        path.cubicTo(80, 150, 150, 220, 220, 180);
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        canvas.drawPath(path, paint);

        //곡선 패스 위에 텍스트 출력
        paint.setTextSize(20);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawTextOnPath("Curved Text on Path.", path, 0, 0, paint);


    }
}
