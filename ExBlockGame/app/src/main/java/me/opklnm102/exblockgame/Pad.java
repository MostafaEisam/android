package me.opklnm102.exblockgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Administrator on 2016-04-12.
 */
public class Pad implements DrawableItem   {
    private final float mTop;
    private float mLeft;
    private final float mBottom;
    private float mRight;

    public Pad(float top, float bottom){
        mTop = top;
        mBottom = bottom;
    }

    public void setLeftRight(float left, float right){
        mLeft = left;
        mRight = right;
    }

    public void draw(Canvas canvas, Paint paint){
        //색칠하는 부분 그리기
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mLeft, mTop, mRight, mBottom, paint);
    }

    //패드의 최상단 식별
    public float getTop(){
        return mTop;
    }
}
