package com.github.amitsk.todos.controllers;

import com.github.amitsk.todos.model.SunsetSunrise;
import com.github.amitsk.todos.service.SunriseApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/sunrise")
public class SunriseController {

    private final SunriseApiClient sunriseApiClient;

    @Autowired
    public SunriseController(SunriseApiClient sunriseApiClient) {
        this.sunriseApiClient = sunriseApiClient;
    }


    @GetMapping("/{lat}/{lng}")
    public Mono<SunsetSunrise> getSunsetSunrise(@PathVariable Double lat, @PathVariable Double lng) {
        return sunriseApiClient.callApi(lat, lng);
    }
}
