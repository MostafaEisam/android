package me.dong.simple_android_graph.component;

import dagger.Component;
import me.dong.simple_android_graph.PreActivity;
import me.dong.simple_android_graph.module.ActivityModule;
import me.dong.simple_android_graph.ui.HomeActivity;
import me.dong.simple_android_graph.ui.HomeFragment;

/**
 * AbstractActivityComponent 상속하고 @PreActivity 스코프에서 작동하는 컴포넌트
 * 유저와 관련된 프래그먼트들에게 객체들을 주입하기 위해 사용
 *
 * ActivityModule이 액티비티를 그래프에 제공하기 때문에, Dagger가 필요할 때마다
 * ActivityModule로부터 컨텍스트를 가져와 주입 -> 하위 모듈에 액티비티를 재정의할 필요가 없다.
 */
@PreActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface HomeComponent extends AbstractActivityComponent {
    void inject(HomeActivity homeActivity);

    void inject(HomeFragment homeFragment);
}
