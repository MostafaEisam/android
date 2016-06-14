package me.opklnm102.interpolator;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Administrator on 2016-06-14.
 */
public class AnimView extends View {

    Ball mBall;

    public AnimView(Context context) {
        super(context);
        mBall = new Ball();
        mBall.setX(100);
        mBall.setY(50);
        mBall.setRadius(20);
    }

    public void startAnim(TimeInterpolator interpol){
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBall, "x", 100, 400);
        objectAnimator.setDuration(4000);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //주기적으로 화면을 다시 그린다.
                //x를 바꾼다고 화면이 다시 그려지는게 아니므로 호출
                invalidate();
            }
        });
        objectAnimator.setInterpolator(interpol);
        objectAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mBall.getColor());
        paint.setAntiAlias(true);
        canvas.drawCircle(mBall.getX(), mBall.getY(), mBall.getRadius(), paint);
    }
}