package com.github.amitsk.sunrise.service;

import com.github.amitsk.sunrise.model.SunriseRequest;
import com.github.amitsk.sunrise.model.SunsetApiResponse;
import com.github.amitsk.sunrise.model.SunsetSunrise;
import com.nike.backstopper.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Component
public class SunriseApiClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SunriseService sunriseService;

    @Autowired
    public SunriseApiClient(SunriseService sunriseService) {
        this.sunriseService = sunriseService;
    }

    public Mono<SunsetSunrise> callApi(SunriseRequest sunriseRequest) {
        logger.info("Invoking call for lat={}, lng = {} and URL {}", sunriseRequest.getLat(), sunriseRequest.getLng());

        return Mono.<SunsetApiResponse>create(sink -> sunriseService.sunrise(sunriseRequest.getLat(),
                sunriseRequest.getLng()).enqueue(
                new Callback<SunsetApiResponse>() {
                    @Override
                    public void onResponse(Call<SunsetApiResponse> call, Response<SunsetApiResponse> response) {

                        if (response.isSuccessful()) {
                            sink.success(response.body());
                        }
                        else {
                            call.cancel();
                            sink.error(ApiException.newBuilder()
                                    .withExceptionCause(new RuntimeException("Error called")).build());
                        }
                    }
                    @Override
                    public void onFailure(Call<SunsetApiResponse> call, Throwable t) {
                        sink.error(t);
                    }
                }
        )).map(SunsetApiResponse::toSunsetSunrise);
    }

}
