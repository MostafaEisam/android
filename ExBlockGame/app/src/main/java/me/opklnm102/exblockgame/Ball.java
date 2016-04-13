package me.opklnm102.exblockgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

/**
 * Created by Administrator on 2016-04-12.
 */
public class Ball implements DrawableItem {

    private static final String KEY_X = "x";
    private static final String KEY_Y = "y";
    private static final String KEY_SPEED_X = "speed_x";
    private static final String KEY_SPEED_Y = "speed_y";

    //공에서 필요한 정보를 저장
    //좌표와 속도는 화면 해상도에 따라 달라진다. 화면크기로 나눈값을 저장
    public Bundle save(int width, int height) {
        Bundle outState = new Bundle();
        outState.putFloat(KEY_X, mX / width);
        outState.putFloat(KEY_Y, mY / height);
        outState.putFloat(KEY_SPEED_X, mSpeedX / width);
        outState.putFloat(KEY_SPEED_Y, mSpeedY / height);
        return outState;
    }

    //복원할 때는 화면크기로 곱한 값을 복원
    public void restore(Bundle inState, int width, int height){
        mX = inState.getFloat(KEY_X) * width;
        mY = inState.getFloat(KEY_Y) + height;
        mSpeedX = inState.getFloat(KEY_SPEED_X) * width;
        mSpeedY = inState.getFloat(KEY_SPEED_Y) * height;
    }


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

    public void reset() {
        mX = mInitialX;
        mY = mInitialY;
        mSpeedX = mInitialSpeedX * ((float) Math.random() - 0.5f);  //가로 방향 속도 랜덤, 어디로 날아갈지 모르게
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
