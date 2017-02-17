package me.dong.exrxbus;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dong on 2017-02-17.
 */
public class BaseActivity extends AppCompatActivity {

    private Subscription mBusSubscription;

    @Override
    protected void onStart() {
        super.onStart();
        autoUnsubscribeBus();
        mBusSubscription = RxEventBus.getInstance().getBusObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if(o == null){
                        return;
                    }
                    handleBus(o);
                }, throwable -> {
                    // onError
                    handleError(throwable);
                }, () -> {
                    // onCompleted
                    handleCompleted();
                });
    }

    @Override
    protected void onStop() {
        autoUnsubscribeBus();
        super.onStop();
    }

    protected void handleBus(@NonNull Object o) {
    }

    protected void handleError(Throwable t){
    }

    protected void handleCompleted(){
    }

    private void autoUnsubscribeBus() {
        if (mBusSubscription != null && !mBusSubscription.isUnsubscribed()) {
            mBusSubscription.unsubscribe();
        }
    }
}