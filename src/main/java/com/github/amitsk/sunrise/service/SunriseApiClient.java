package com.github.amitsk.sunrise.service;

import com.github.amitsk.sunrise.model.SunsetApiResponse;
import com.github.amitsk.sunrise.model.SunriseRequest;
import com.github.amitsk.sunrise.model.SunsetSunrise;
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

    public Mono<SunsetSunrise> callApi(SunriseRequest sunriseRequest) {
        logger.info("Invoking call for lat={}, lng = {} and URL {}", sunriseRequest.getLat(), sunriseRequest.getLng());
        return sunriseWebClient.get()
                .uri(builder ->
                         builder
                        .queryParam("lat", sunriseRequest.getLat())
                        .queryParam("lng", sunriseRequest.getLng())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SunsetApiResponse.class)
                .map(SunsetApiResponse::toSunsetSunrise);
    }

}
