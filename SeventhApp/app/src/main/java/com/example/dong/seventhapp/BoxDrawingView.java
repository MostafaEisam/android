package com.example.dong.seventhapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Dong on 2015-07-15.
 */
/*
단순뷰이면서 View의 직계 서브클래스
사용자의 화면터치(touch)와 드래깅(dragging)에 대한 응답을 박스로 그린다.
 */
public class BoxDrawingView extends View {
    public static final String TAG = "BoxDrawingView";

    private ArrayList<Box> mBoxes = new ArrayList<Box>();
    private Box mCurrentBox;
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    //이 2가지 생성자들은 사용할 계획이 없더라도 일단 정의해 두는 것이 좋다.

    //코드에서 뷰를 생성할 때 사용한다.
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    //XML로부터 뷰를 인플레이트할 때 사용한다.
    //AttributeSet -> XML에 지정된 속성들을 포함
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //반투명의 붉은색으로 박스를 그린다.
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        //배경을 황백색으로 칠한다.
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //배경을 채운다.
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxes) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //터치된 x,y의 좌표를 PointF객체에 넣는다.
        PointF curr = new PointF(event.getX(), event.getY());

        Log.i(TAG, "Received evnet at x=" + curr.x
                + ", y=" + curr.y + ":");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "ACTION_DOWN");
                //그리기 상태를 재설정한다.
                mCurrentBox = new Box(curr);
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "ACTION_MOVE");
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(curr);
                    //BoxDrawingView를 다시 부적합(invalid)상태로 만들고
                    //BoxDrawingView는 자신을 다시 그리게 되고 onDraw()가 다시 호출
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION_UP");
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "ACTION_CANCEL");
                mCurrentBox = null;
                break;
        }

        return true;
    }
}
