package com.example.dong.doitmission_10;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Created by Dong on 2015-04-01.
 */
public class CoverFlow extends Gallery {
    private Camera camera = new Camera();

    public static int maxRotationAngle = 55;

    public static int maxZoom = -60;

    private int centerPoint;

    Book book[] = new Book[5];

    public CoverFlow(Context context) {
        super(context);

        init();
    }

    public CoverFlow(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CoverFlow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    /**
     * 초기화
     */
    private void init() {
        this.setStaticTransformationsEnabled(true);
        book[0] = new Book("미움받을 용기", "인플루엔셜", "2014.11.17", "기시미 이치로");
        book[1] = new Book("지적대화를 위한 넓고 얕은 지식", "한빛비즈", "2014.12.05", "채사장");
        book[2] = new Book("마법천자문 31", "아울북", "2015.03.23", "올댓스토리");
        book[3] = new Book("7번읽기 공부법", "위즈덤하우스", "2015.03.26", "야마구치 마유");
        book[4] = new Book("그림의 힘", "에이트포인트", "2015.03.02", "김선현");
    }

    public int getMaxRotationAngle() {
        return maxRotationAngle;
    }

    public void setMaxRotationAngle(int rotationAngle) {
        maxRotationAngle = rotationAngle;
    }

    public int getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(int zoom) {
        maxZoom = zoom;
    }

    public int getCenterOfCoverflow() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
    }

    private static int getCenterOfView(View view) {
        return view.getLeft() + view.getWidth() / 2;
    }

    protected boolean getChildStaticTransformation(View child, Transformation t) {

        final int childCenter = getCenterOfView(child);
        final int childWidth = child.getWidth();
        int rotationAngle = 0;
        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX);

        if (childCenter == centerPoint) {
            transformImageBitmap((ImageView) child, t, 0);

        } else {
            rotationAngle = (int) (((float) (centerPoint - childCenter) / childWidth) * maxRotationAngle);
            if (Math.abs(rotationAngle) > maxRotationAngle) {
                rotationAngle = (rotationAngle < 0) ? -maxRotationAngle : maxRotationAngle;
            }
            transformImageBitmap((ImageView) child, t, rotationAngle);
        }

        return true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        centerPoint = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void transformImageBitmap(ImageView child, Transformation t, int rotationAngle) {
        camera.save();

        final Matrix imageMatrix = t.getMatrix();
        ;
        final int imageHeight = child.getLayoutParams().height;
        ;
        final int imageWidth = child.getLayoutParams().width;
        final int rotation = Math.abs(rotationAngle);

        camera.translate(0.0f, 0.0f, 100.0f);

        if (rotation < maxRotationAngle) {
            float zoomAmount = (float) (maxZoom + (rotation * 1.5));
            camera.translate(0.0f, 0.0f, zoomAmount);
        }

        camera.rotateY(rotationAngle);
        camera.getMatrix(imageMatrix);

        imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
        imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));

        camera.restore();
    }
}
