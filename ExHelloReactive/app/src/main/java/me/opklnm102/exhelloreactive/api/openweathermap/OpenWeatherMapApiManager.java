package me.opklnm102.exhelloreactive.api.openweathermap;

import com.google.gson.Gson;

import me.opklnm102.exhelloreactive.api.openweathermap.models.WeatherResponse;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-12.
 */
public class OpenWeatherMapApiManager {

    private static OpenWeatherMapApiManager instance;

    private final OpenWeatherMapService mOpenWeatherMapService;

    public static OpenWeatherMapApiManager getInstance(){
        if(instance == null){
            synchronized (OpenWeatherMapApiManager.class){
                if(instance == null){
                    instance = new OpenWeatherMapApiManager();
                }
            }
        }
        return instance;
    }

    private OpenWeatherMapApiManager(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mOpenWeatherMapService = retrofit.create(OpenWeatherMapService.class);
    }

    public Observable<WeatherResponse> getForecastByCity(String city){
        return mOpenWeatherMapService
                .getForecastByCity(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
