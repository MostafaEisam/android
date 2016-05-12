package me.opklnm102.exhelloreactive.api.stackexchange;

import me.opklnm102.exhelloreactive.api.stackexchange.models.UsersResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016-05-12.
 */
public interface StackExchangeService {

    @GET("2.2/users?order=desc&pagesize=10&sort=reputation&site=stackoverflow")
    Observable<UsersResponse> getTenMostPopularSOusers();

    @GET("2.2/users?order=desc&sort=reputation&site=stackoverflow")
    Observable<UsersResponse> getMostPopularSOusers(@Query("pagesize") int howmany);
}
