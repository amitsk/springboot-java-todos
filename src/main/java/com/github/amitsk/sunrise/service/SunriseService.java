package com.github.amitsk.sunrise.service;

import com.github.amitsk.sunrise.model.SunsetApiResponse;
import reactor.core.publisher.Mono;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SunriseService {

    @GET("/")
    Mono<SunsetApiResponse> sunrise(@Query("lat") String lat, @Query("lng") String lng);
}
