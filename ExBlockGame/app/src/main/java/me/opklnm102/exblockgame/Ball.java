package me.opklnm102.exblockgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Administrator on 2016-04-12.
 */
public class Ball implements DrawableItem {

    private float mX;
    private float mY;
    private float mSpeedX;  //좌표의 이동너비
    private float mSpeedY;
    private final float mRadius;

    //초기속도
    private final float mInitialSpeedX;
    private final float mInitialSpeedY;

    //출현 위치
    private final float mInitialX;
    private final float mInitialY;

    public Ball(float radius, float initialX, float initialY) {
        mRadius = radius;
        mSpeedY = -radius / 5;
        mSpeedX = radius / 5;
        mY = initialY;
        mX = initialX;

        mInitialSpeedX = mSpeedX;
        mInitialSpeedY = mSpeedY;
        mInitialX = mX;
        mInitialY = mY;
    }

    public void reset(){
        mX = mInitialX;
        mY = mInitialY;
        mSpeedX = mInitialSpeedX * ((float)Math.random() - 0.5f);  //가로 방향 속도 랜덤, 어디로 날아갈지 모르게
        mSpeedY = mInitialSpeedY;
    }

    public void move() {
        mX += mSpeedX;
        mY += mSpeedY;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mX, mY, mRadius, paint);
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public float getSpeedX() {
        return mSpeedX;
    }

    public float getSpeedY() {
        return mSpeedY;
    }

    public void setSpeedX(float speedX) {
        mSpeedX = speedX;
    }

    public void setSpeedY(float speedY) {
        mSpeedY = speedY;
    }


}
