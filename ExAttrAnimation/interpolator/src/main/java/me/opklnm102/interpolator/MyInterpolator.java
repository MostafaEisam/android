package me.opklnm102.interpolator;

import android.animation.TimeInterpolator;

/**
 * 역방향으로 리니어 보간 수행
 * 삼각함수, 로그함수 등을 사용하면 곡선형 보간도 가능
 */
public class MyInterpolator implements TimeInterpolator {
    @Override
    public float getInterpolation(float input) {
        return 1-input;
    }
}
