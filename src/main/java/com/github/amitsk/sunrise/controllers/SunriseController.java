package com.github.amitsk.sunrise.controllers;

import com.github.amitsk.sunrise.model.SunriseRequest;
import com.github.amitsk.sunrise.model.SunsetSunrise;
import com.github.amitsk.sunrise.service.SunriseApiClient;
import com.github.amitsk.sunrise.service.SunriseService;
import com.nike.backstopper.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import retrofit2.Retrofit;

import javax.validation.Valid;
import javax.validation.Validator;

import static com.github.amitsk.sunrise.errors.SunriseApiError.GENERIC_BAD_REQUEST;

@RestController
@RequestMapping(value = "/sunrise")
public class SunriseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SunriseApiClient sunriseApiClient;

    @Autowired
    private Validator validator;

    @Autowired
    public SunriseController(SunriseApiClient sunriseApiClient)
    {
        this.sunriseApiClient = sunriseApiClient;
    }



    @GetMapping("/{lat}/{lng}")
    @Valid
    public Mono<SunsetSunrise> getSunsetSunrise(
            @PathVariable String lat,
            @PathVariable String lng) {
        //TODO Plugin Validation
        SunriseRequest sunriseRequest = new SunriseRequest(lat, lng);
        logger.info("Sunrise Request passed {}", sunriseRequest);

        if(!validator.validate(sunriseRequest).isEmpty()) {
            throw ApiException.newBuilder()
                    .withApiErrors(GENERIC_BAD_REQUEST).build();
        }
        return sunriseApiClient.callApi(sunriseRequest);
    }
}
