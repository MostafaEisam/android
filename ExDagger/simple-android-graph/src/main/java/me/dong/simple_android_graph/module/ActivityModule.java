package me.dong.simple_android_graph.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import me.dong.simple_android_graph.PreActivity;

/**
 * 그래프의 자손들에게 액티비티를 제공
 * ex. Fragment에서 Activity Context 사용
 */
@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PreActivity
    Activity provideActivity() {
        return mActivity;
    }
}
