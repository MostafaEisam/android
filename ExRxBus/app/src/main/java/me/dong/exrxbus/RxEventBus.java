package me.dong.exrxbus;


import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;



public class RxEventBus {

    private static RxEventBus instance;

    public static RxEventBus getInstance() {
        if (instance == null) {
            instance = new RxEventBus();
        }
        return instance;
    }

    private RxEventBus() {
    }

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    public void post(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> getBusObservable() {
        return bus;
    }

    public boolean hasObservers(){
        return bus.hasObservers();
    }
}