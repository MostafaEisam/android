package me.opklnm102.excustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016-04-28.
 */
public class RainbowProgress extends View {

    int mMax;
    int mPos;
    int mProgHeight;
    LinearGradient mShader;

    public RainbowProgress(Context context) {
        super(context);
        init();
    }

    public RainbowProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RainbowProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        mMax = 100;
        mPos = 0;
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        if(max > 0){
            mMax = max;
            invalidate();
        }
    }

    public int getPos() {
        return mPos;
    }

    public void setPos(int pos) {
        if(pos < 0 || pos > mMax){
            return;
        }
        mPos = pos;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mShader == null){
            mProgHeight = getHeight() - getHeight() - getPaddingTop() - getPaddingBottom();
            int[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};
            mShader = new LinearGradient(0,0,0,mProgHeight, colors, null, Shader.TileMode.CLAMP);
        }

        RectF rt = new RectF();
        rt.left = getPaddingLeft();
        rt.right = getWidth() - getPaddingRight();
        rt.bottom = getPaddingTop() + mProgHeight;
        rt.top = rt.bottom - mProgHeight * mPos / mMax;

        Paint fillPaint = new Paint();
        fillPaint.setShader(mShader);
        canvas.drawRect(rt, fillPaint);

        rt.top = getPaddingTop();
        Paint outPaint = new Paint();
        outPaint.setColor(Color.WHITE);
        outPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rt, outPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 48, height = 200;

        switch (MeasureSpec.getMode(widthMeasureSpec)){
            case MeasureSpec.AT_MOST:
                width = Math.min(MeasureSpec.getSize(widthMeasureSpec), width);
                break;
            case MeasureSpec.EXACTLY:
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }

        switch (MeasureSpec.getMode(heightMeasureSpec)){
            case MeasureSpec.AT_MOST:
                height = Math.min(MeasureSpec.getSize(heightMeasureSpec), height);
                break;
            case MeasureSpec.EXACTLY:
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }

        setMeasuredDimension(width, height);
    }
}
