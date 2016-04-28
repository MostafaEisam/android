package me.opklnm102.excustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/*
  완전히 새로운 위젯을 만들 때는 View를 상속
  View - 화면에 모습을 그리기 위한 메소드, 이벤트 핸들러의 기본원형 제공
         그러나 기능이 너무 일반적이어서 자체로는 쓸모가 없기 때문에 몇가지 메소드를 반드시 재정의
         ex) onDraw() - View는 사각영역일 뿐 화면에는 아무것도 그리지 않으므로 반드시 재정의
             onMeasure() - 위젯의 크기를 결정.
                           부모 레이아웃은 차일드를 배치하기 위해 각 위젯의 크기를 조사하는데
                           이때 차일드의 measure()를 호출
                           measure()는 강제 레이아웃, 크기변경빈도 최소화, 치명적인 에러 처리등의 중요한 역할을 담당하므로 직접 재정의하는 것은 바람직하지 않다.
                           대신 measure()는 크기 결정시 onMeasure()를 호출하므로 onMeasure()를 재정의
   void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
   인수로 전달된 Spec은 부모 레이아웃이 차일드에게 제공하는 여유공간의 폭과 높이에 대한 정보(공간의 성질을 지정하는 모드와 공간의 크기값이 저장).

   모드 - AT_MOST
          차일드가 가질 수 있는 최대 크기.
          차일드는 이 값 이하로 크기를 결정.

          EXACTLY
          차일드가 가져야하는 정확한 크기
          특별한 사유가 없는한 이 크기를 가져야 한다.

          UNSPECIFIED
          별다른 제한이 없으므로 원하는 크기를 지정
   ex) 폭이 AT_MOST 200, 높이가 EXACTLY 100이면 폭을 200이하로 결정하고 높이는 가급적 100으로 맞추어야한다.

          부모는 차일드의 onMeasure()를 호출하되 남은 공간에 대한 정보를 Spec인수로 전달하고
          차일드는 이 정보를 참조하여 적절한 크기를 선택하여 부모에게 응답
          void setMeasureDimension(int measureWidth, int measureHeight)를 사용하여 응답한다.
          이 메소드를 호출하지 않으면 IllegalStateException발생
 */
public class MeasView extends View {

    String strResult = "";

    public MeasView(Context context) {
        super(context);
    }

    public MeasView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    //픽셀 사용
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode, hMode;
        int wSpec, hSpec;
        int width, height;

        width = 150;
        height = 80;

        wMode = MeasureSpec.getMode(widthMeasureSpec);
        wSpec = MeasureSpec.getSize(widthMeasureSpec);
        hMode = MeasureSpec.getMode(heightMeasureSpec);
        hSpec = MeasureSpec.getSize(heightMeasureSpec);

        switch (wMode) {
            case MeasureSpec.AT_MOST:  //자신이 원하는 폭과 부모가 허락한 폭의 최소값을 취한다.
                width = Math.min(wSpec, width);
                break;
            case MeasureSpec.EXACTLY:  //부모가 제안한 크기 사용
                width = wSpec;
                break;
            case MeasureSpec.UNSPECIFIED:  //자신이 원하는 크기
                break;
        }

        switch (hMode) {
            case MeasureSpec.AT_MOST:  //wrap_content
                height = Math.min(hSpec, height);
                break;
            case MeasureSpec.EXACTLY:  //match_parent
                height = hSpec;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        //종횡비 유지. 시계처럼 유지해야하는 경우.
        if(wMode == MeasureSpec.AT_MOST && hMode == MeasureSpec.AT_MOST){
            width = height = Math.min(width, height);
        }

        setMeasuredDimension(width, height);  //부모에게 전달

        strResult += (spec2String(widthMeasureSpec) + ", " + spec2String(heightMeasureSpec) + " -> (" + width + ", " + height + ")\n");

    }

    private String spec2String(int spec) {
        String str = "";

        switch (MeasureSpec.getMode(spec)) {
            case MeasureSpec.AT_MOST:
                str = "AT_MOST";
                break;
            case MeasureSpec.EXACTLY:
                str = "EXACTLY";
                break;
            default:
                str = "UNSPECIFIED";
                break;
        }
        str += " " + MeasureSpec.getSize(spec);
        return str;
    }
}
