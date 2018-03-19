package com.github.amitsk.sunrise.service;

import com.github.amitsk.sunrise.model.SunriseRequest;
import com.github.amitsk.sunrise.model.SunsetApiResponse;
import com.github.amitsk.sunrise.model.SunsetSunrise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SunriseApiClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SunriseService sunriseService;

    @Autowired
    public SunriseApiClient(SunriseService sunriseService) {
        this.sunriseService = sunriseService;
    }

    public Mono<SunsetSunrise> callApi(SunriseRequest sunriseRequest) {
        logger.info("Invoking call for lat={}, lng = {} ", sunriseRequest.getLat(), sunriseRequest.getLng());

        return sunriseService.sunrise(sunriseRequest.getLat(),
                sunriseRequest.getLng())
                .map(SunsetApiResponse::toSunsetSunrise);
    }

}
