package me.dong.simple_android_graph.ui;

import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.inject.Inject;

import me.dong.simple_android_graph.DemoApplication;
import me.dong.simple_android_graph.R;
import me.dong.simple_android_graph.component.DaggerHomeComponent;
import me.dong.simple_android_graph.component.HomeComponent;
import me.dong.simple_android_graph.module.ActivityModule;

/**
 * Dagger는 의존성을 주입하는 다양한 옵션
 * 1. 생성자 주입 - 클래스 생성자에 @Inject를 붙인다.
 * 2. 필드 주입 - 클래스의 private 식별자가 아닌 필드에 @Inject를 붙인다.
 * 3. 메소드 주입 - 메소드에 @Inject를 붙인다.
 * 위의 순서대로 접근한다.
 * 안드로이드는 생성자 주입을 할 수 없기 때문에 주로 필드 주입을 한다.
 */
public class HomeActivity extends AppCompatActivity {

    @Inject
    LocationManager mLocationManager;

    private HomeComponent component;

    HomeComponent component() {
        if (component == null) {
            component = DaggerHomeComponent.builder()
                    .applicationComponent(((DemoApplication) getApplication()).component())
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return component;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new HomeFragment())
                    .commit();
        }
    }

    // TODO do something with the injected dependencies here!
}
