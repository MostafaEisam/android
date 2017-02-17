package me.dong.exrxbus;

import android.support.v4.app.Fragment;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Dong on 2017-02-17.
 */
public class BaseFragment extends Fragment {

    private Subscription mBusSubscription;

    @Override
    public void onStart() {
        super.onStart();
        autoUnsubscribeBus();
        mBusSubscription = RxEventBus.getInstance().getBusObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                            if (o == null) {
                                return;
                            }
                            handleBus(o);
                        },
                        this::handleError,
                        this::handleCompleted);
    }

    protected void handleBus(Object o) {
    }

    protected void handleError(Throwable t) {
    }

    protected void handleCompleted() {
    }

    private void autoUnsubscribeBus() {
        if (mBusSubscription != null && !mBusSubscription.isUnsubscribed()) {
            mBusSubscription.unsubscribe();
        }
    }

    @Override
    public void onStop() {
        autoUnsubscribeBus();
        super.onStop();
    }
}