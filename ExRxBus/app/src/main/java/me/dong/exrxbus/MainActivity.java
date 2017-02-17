package me.dong.exrxbus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * 다음의 포스트를 따라한 예제
 * https://quangson8128.github.io/2016/06/23/rxbus-android/
 * https://quangson8128.github.io/2016/06/23/android-event-bus-with-rxjava-tips/
 * <p>
 * 이걸 라이브러리로!
 * https://github.com/AndroidKnife/RxBus
 */
public class MainActivity extends BaseActivity {

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextView = (TextView) findViewById(R.id.textView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> RxEventBus.getInstance().post(new Events("onClick " + System.currentTimeMillis())));

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_fragment_container_one, new MainFragment())
                .add(R.id.fl_fragment_container_two, new MainFragment())
                .commit();
    }

    @Override
    protected void handleBus(@NonNull Object o) {
        if (o instanceof Events) {
            mTextView.setText(String.format("Activity %s", ((Events) o).getTag()));
        }
    }

    @Override
    protected void handleError(Throwable t) {
        mTextView.setText("onError" + t.getMessage());
    }

    @Override
    protected void handleCompleted() {
        mTextView.setText("onCompleted");
    }
}