package kr.co.mash_up.excanvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import kr.co.mash_up.excanvas.R;

/**
 * 커스텀뷰를 정의하는 기본적인 형태
 * 생성자와 onDraw()를 오버라이딩
 */
public class MyViewBasic extends View {

    public MyViewBasic(Context context) {
        super(context);
    }

    /*
      그리기 코드
      캔버스에 그래픽을 출력하는 모든 메소드는 픽셀단위 사용
      점단위로 섬세하게 그래픽을 그릴 수 있어 자유도가 높다
      물리적인 단위를 사용하므로 레이아웃에 비해 호환성에는 불리
     */

    /*
      Canvas
      View의 그리기 표면이며 이 위에 그림을 그린다.
      그리기를 할 때마다 시스템이 캔버스 객체를 생성 및 초기화하여 뷰의 onDraw()로 전달하므로
      따로 생성할 필요 없이 전달받은 객체를 사용
      다양한 그리기 메소드 제공
       public void drawPoint(float x, float y, Paint paint)
       public void drawLine(float startX, float startY, float stopX, float stopY, Paint paint)
       public void drawCircle(float cx, float cy, float radius, Paint paint)
       public void drawRect(float left, float top, float right, float bottom, Paint paint)
       public void drawText(String text, float x, float y, Paint paint) 등...
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
        Paint paint = new Paint();
        //검은색 점
        canvas.drawPoint(10, 10, paint);
        //파란색 선
        paint.setColor(Color.BLUE);
        canvas.drawLine(20, 10, 200, 50, paint);
        //빨간색 원
        paint.setColor(Color.RED);
        canvas.drawCircle(100, 90, 50, paint);
        //반투명한 파란색 사각형
        paint.setColor(0x800000ff);
        canvas.drawRect(10, 100, 200, 170, paint);
        //검은색 문자열
        paint.setColor(Color.BLACK);
        canvas.drawText("Canvas Text Out", 10, 200, paint);

        /**
         * 화면 대응. 160dp 적용
         */
        int y = 1;

        //1. 원론적인 방법
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = (int) (160 * dm.density);
        int height = (int) (60 * dm.density);
        canvas.drawRect(0, y, width, y + height, paint);
        y += (height * 1.2f);

        //2. dimen 리소스 사용
        width = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        height = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        canvas.drawRect(0, y, width, y + height, paint);
        y += (height * 1.2f);

        //직접 계산
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, dm);
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, dm);
        canvas.drawRect(0, y, width, y + height, paint);
        y += (height * 1.2f);
    }
}
