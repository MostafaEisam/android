package me.opklnm102.exhelloreactive.api.stackexchange;


import java.util.List;

import me.opklnm102.exhelloreactive.api.stackexchange.models.User;
import me.opklnm102.exhelloreactive.api.stackexchange.models.UsersResponse;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SeApiManager {

    private final StackExchangeService mStackExchangeService;

    private static SeApiManager instance;

    public static SeApiManager getInstance(){
        if(instance == null){
            synchronized (SeApiManager.class){
                if (instance == null){
                    instance = new SeApiManager();
                }
            }
        }
        return instance;
    }

    private SeApiManager(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mStackExchangeService = retrofit.create(StackExchangeService.class);
    }

    public Observable<List<User>> getTenMostPopularSOusers() {
        return mStackExchangeService
                .getTenMostPopularSOusers()
                .map(UsersResponse::getUsers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<User>> getMostPopularSOusers(int howmany) {
        return mStackExchangeService
                .getMostPopularSOusers(howmany)
                .map(UsersResponse::getUsers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
