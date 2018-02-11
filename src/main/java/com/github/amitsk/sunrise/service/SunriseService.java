package com.github.amitsk.sunrise.service;

import com.github.amitsk.sunrise.model.SunsetApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SunriseService {

    @GET("/")
    Call<SunsetApiResponse> sunrise(@Query("lat") String lat, @Query("lng") String lng);
}
