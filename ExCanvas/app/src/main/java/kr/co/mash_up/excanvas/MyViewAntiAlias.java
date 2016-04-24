package kr.co.mash_up.excanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by Dong on 2016-04-25.
 */
public class MyViewAntiAlias extends View{

    public MyViewAntiAlias(Context context) {
        super(context);
    }

    /*
       Paint
       그리기에 대한 속성 정보를 가지며 모든 그리기 메소드에 인수로 전달

       AntiAlias
       색상차가 뚜렷한 경계 부근에 중간색을 삽입하여 도형이나 글꼴이 주변 배경과 부드럽게 잘 어울리도록 하는 그래픽 출력 기법
       출력 품질은 향상되지만 중간색상을 삽입하기 위한 연산으로 속도가 떨어진다.
       사용하면 훨씬더 부드러워 보인다.
       요즘은 성능보다 품질을 중요시하므로 왠만하면 사용
       new Paint(Paint.ANTI_ALIAS_FLAG)를 사용하여 객체생성시 설정 가능
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.LTGRAY);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(60);

        //기본 출력
        canvas.drawOval(new RectF(10, 10, 200, 120), paint);
        canvas.drawText("Text", 210, 70, paint);

        //안티 알리아스 적용
        paint.setAntiAlias(true);
        canvas.drawOval(new RectF(10, 130, 200, 240), paint);
        canvas.drawText("Text", 210, 190, paint);
    }
}
