package me.opklnm102.xml;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

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

    public void startAnim() {
        mBall.setX(100);
        mBall.setY(50);
        mBall.setRadius(20);

        /**
         * target과 listener는 XML로 지정할 수 없으므로 전개한 후 메소드를 호출하여 지정
         */
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.right_down_large);
        set.setTarget(mBall);

        ArrayList<Animator> childs = set.getChildAnimations();
        ((ObjectAnimator)childs.get(0)).addUpdateListener(mAnimatorUpdateListener);
        ((ObjectAnimator)childs.get(1)).addUpdateListener(mAnimatorUpdateListener);
        ((ObjectAnimator)childs.get(2)).addUpdateListener(mAnimatorUpdateListener);

        set.start();
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