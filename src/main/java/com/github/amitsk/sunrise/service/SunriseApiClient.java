package com.github.amitsk.sunrise.service;

import com.github.amitsk.sunrise.model.SunriseRequest;
import com.github.amitsk.sunrise.model.SunsetApiResponse;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class SunriseApiClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WebClient webClient;

    public SunriseApiClient(WebClient webClient) {
        this.webClient = webClient;
    }



    @Timed
    public Mono<SunsetApiResponse.SunsetSunrise> callApi(SunriseRequest sunriseRequest) {
        logger.info("Invoking call for lat={}, lng = {} ", sunriseRequest.lat(), sunriseRequest.lng());
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/json")
                        .queryParam("lat", sunriseRequest.lat())
                        .queryParam("lng", sunriseRequest.lng())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SunsetApiResponse.class)
                .map(SunsetApiResponse::toSunsetSunrise);
    }

}
