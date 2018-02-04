package com.github.amitsk.todos.service;

import com.github.amitsk.todos.model.SunsetSunrise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class SunriseApiClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WebClient sunriseWebClient;

    @Autowired
    public SunriseApiClient(@Qualifier("sunriseWebClient") WebClient webClient) {
        this.sunriseWebClient = webClient;
    }

    public Mono<SunsetSunrise> callApi(Double lat, Double lng) {
        logger.info("Invoking call for lat={}, lng = {}", lat, lng);
        return sunriseWebClient.get()
                .uri(builder -> builder
                        .replacePath("")
                        .queryParam("lat", lat)
                        .queryParam("lng", lng)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SunsetSunrise.class);
    }

}
