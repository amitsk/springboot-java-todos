package com.github.amitsk.sunrise.controllers;

import com.github.amitsk.sunrise.model.SunriseRequest;
import com.github.amitsk.sunrise.model.SunsetApiResponse;
import com.github.amitsk.sunrise.service.SunriseApiClient;
import com.nike.backstopper.exception.ApiException;
import io.micrometer.core.annotation.Timed;
import io.micrometer.tracing.Tracer;
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

    @Autowired
    private Validator validator;

    @Autowired
    public SunriseController(SunriseApiClient sunriseApiClient, Validator validator) {
        this.sunriseApiClient = sunriseApiClient;
        this.validator = validator;
    }


    @GetMapping("/{lat}/{lng}")
    @Timed
    public Mono<SunsetApiResponse.SunsetSunrise> getSunsetSunrise(
            @PathVariable String lat,
            @PathVariable String lng) {

        SunriseRequest sunriseRequest = new SunriseRequest(lat, lng);
        logger.info("Sunrise Request passed {}", sunriseRequest);

        Set<ConstraintViolation<SunriseRequest>> errors = validator.validate(sunriseRequest);
        if (!errors.isEmpty()) {
            throw ApiException.newBuilder()
                    .withApiErrors(GENERIC_BAD_REQUEST).build();
        }

        Mono<SunsetApiResponse.SunsetSunrise> sunsetSunriseMono =
                sunriseApiClient.callApi(sunriseRequest);

        return sunsetSunriseMono;
    }
}
