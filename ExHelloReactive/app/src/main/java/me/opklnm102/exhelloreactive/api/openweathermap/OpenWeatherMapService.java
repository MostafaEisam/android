package me.opklnm102.exhelloreactive.api.openweathermap;

import me.opklnm102.exhelloreactive.api.openweathermap.models.WeatherResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016-05-12.
 */
public interface OpenWeatherMapService {

    @GET("data/2.5/weather")
    Observable<WeatherResponse> getForecastByCity(@Query("q") String city);
}
