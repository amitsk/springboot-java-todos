package com.github.amitsk.sunrise.service;

import com.github.amitsk.sunrise.model.SunriseRequest;
import com.github.amitsk.sunrise.model.SunsetApiResponse;
import com.github.amitsk.sunrise.model.SunsetSunrise;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.scheduler.forkjoin.ForkJoinPoolScheduler;

import java.util.concurrent.ForkJoinPool;

@Component
public class SunriseApiClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WebClient webClient;
    private final Scheduler forkJoinPoolScheduler;

    @Autowired
    public SunriseApiClient(WebClient webClient, Scheduler forkJoinPoolScheduler) {
        this.webClient = webClient;
        this.forkJoinPoolScheduler = forkJoinPoolScheduler;
    }

    @Timed
    public Mono<SunsetSunrise> callApi(SunriseRequest sunriseRequest) {
        logger.info("Invoking call for lat={}, lng = {} ", sunriseRequest.getLat(), sunriseRequest.getLng());
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/json")
                        .queryParam("lat", sunriseRequest.getLat())
                        .queryParam("lng", sunriseRequest.getLng())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SunsetApiResponse.class)
                .map(SunsetApiResponse::toSunsetSunrise);
                //.subscribeOn(forkJoinPoolScheduler);
    }

}
