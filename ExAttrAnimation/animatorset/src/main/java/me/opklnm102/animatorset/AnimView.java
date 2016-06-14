package me.opklnm102.animatorset;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.AnimationSet;

/**
 * Created by Administrator on 2016-06-14.
 */
public class AnimView extends View {

    Ball mBall;
    ObjectAnimator mToRight;
    ObjectAnimator mToDown;
    ObjectAnimator mEnLarge;

    public AnimView(Context context) {
        super(context);
        mBall = new Ball();
        mBall.setX(100);
        mBall.setY(50);
        mBall.setRadius(20);

        mToRight = ObjectAnimator.ofInt(mBall, "x", 100, 400);
        mToRight.setDuration(2000);
        mToRight.addUpdateListener(mAnimatorUpdateListener);

        mToDown = ObjectAnimator.ofInt(mBall, "y", 50, 300);
        mToDown.setDuration(2000);
        mToDown.addUpdateListener(mAnimatorUpdateListener);

        mEnLarge = ObjectAnimator.ofInt(mBall, "radius", 20, 40);
        mEnLarge.setDuration(2000);
        mEnLarge.addUpdateListener(mAnimatorUpdateListener);
    }

    public void startAnim(int id) {
        mBall.setX(100);
        mBall.setY(50);
        mBall.setRadius(20);

        AnimatorSet set = new AnimatorSet();
        switch (id){
            case R.id.button1:
                set.playSequentially(mToRight, mToDown, mEnLarge);

//                set.play(mToDown).after(mToRight).before(mEnLarge);

//                set.play(mToRight).before(mToDown);
//                set.play(mToDown).before(mEnLarge);
                break;
            case R.id.button2:


                break;
            case R.id.button3:

                break;
            case R.id.button4:

                break;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mBall.getColor());
        paint.setAntiAlias(true);
        canvas.drawCircle(mBall.getX(), mBall.getY(), mBall.getRadius(), paint);
    }

    ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }
    };
}