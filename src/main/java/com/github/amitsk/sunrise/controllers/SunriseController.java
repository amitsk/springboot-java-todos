package com.github.amitsk.sunrise.controllers;

import com.github.amitsk.sunrise.model.SunriseRequest;
import com.github.amitsk.sunrise.model.SunsetApiResponse;

import com.github.amitsk.sunrise.service.SunriseApiClient;
import com.nike.backstopper.exception.ApiException;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


import java.util.Set;

import static com.github.amitsk.sunrise.errors.SunriseApiError.GENERIC_BAD_REQUEST;

@RestController
@RequestMapping(value = "/sunrise")
public class SunriseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SunriseApiClient sunriseApiClient;
    private final Counter counter;
    private final Counter validationErrorCounter;
    private final Counter successfulRequestCounter;

    @Autowired
    private Validator validator;

    @Autowired
    public SunriseController(SunriseApiClient sunriseApiClient, MeterRegistry registry) {
        this.sunriseApiClient = sunriseApiClient;
        this.counter = registry.counter("received.requests");
        this.validationErrorCounter = registry.counter("validation.error.requests");
        this.successfulRequestCounter = registry.counter("successful.requests");
    }


    @GetMapping("/{lat}/{lng}")
    @Timed
    public Mono<SunsetApiResponse.SunsetSunrise> getSunsetSunrise(
            @PathVariable String lat,
            @PathVariable String lng) {

        counter.increment();
        //TODO Plugin Validation
        SunriseRequest sunriseRequest = new SunriseRequest(lat, lng);
        logger.info("Sunrise Request passed {}", sunriseRequest);

        Set<ConstraintViolation<SunriseRequest>> errors = validator.validate(sunriseRequest);
        if (!errors.isEmpty()) {
            validationErrorCounter.increment();
            throw ApiException.newBuilder()
                    .withApiErrors(GENERIC_BAD_REQUEST).build();
        }

        Mono<SunsetApiResponse.SunsetSunrise> sunsetSunriseMono =
                sunriseApiClient.callApi(sunriseRequest);

        successfulRequestCounter.increment();
        return sunsetSunriseMono;
    }
}
