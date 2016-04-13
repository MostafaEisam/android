package me.opklnm102.exblockgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Administrator on 2016-04-12.
 */
public class Block implements DrawableItem{
    private final float mTop;
    private final float mLeft;
    private final float mBottom;
    private final float mRight;
    private int mHard;  //내구성

    public Block(float top, float left, float bottom, float right) {
        mHard = 1;
        mRight = right;
        mBottom = bottom;
        mLeft = left;
        mTop = top;
    }

    public void draw(Canvas canvas, Paint paint){
        if(isExist()){
            //내구성이 0이상인 경우만 그린다.
            if(mIsCollision){
                mHard--;
                mIsCollision = false;
                if(mHard <= 0){
                    mIsExist = false;
                    return;
                }
            }

           //색칠부분 그리기
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(mLeft, mTop, mRight, mBottom, paint);
            //테두리 선을 그린다.
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4f);
            canvas.drawRect(mLeft, mTop, mRight, mBottom, paint);
        }

    }

    private boolean mIsCollision = false;  //충돌 상태를 기록하는 플래그

    //공이 충돌했을 때 처리
    public void collision(){
        mIsCollision = true;  //충돌 사실만 기록하고 실제 파괴는 draw()시에 한다.
    }

    private boolean mIsExist = true;  //블록이 존재하는가?

    //블록이 존재하는가?
    public boolean isExist(){
        return mIsExist;
    }



}
