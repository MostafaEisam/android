package me.dong.simple_android_graph.component;

import android.app.Activity;

import dagger.Component;
import me.dong.simple_android_graph.PreActivity;
import me.dong.simple_android_graph.module.ActivityModule;

/**
 * A base component upon which fragment's components may depend.  Activity-level components
 * should extend this component.
 *
 * 액티비티의 생명주기 동안 살아 있는 Component
 */
@PreActivity  // Subtypes of AbstractActivityComponent should be decorated with @PerActivity.
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface AbstractActivityComponent {
    Activity activity();
}
