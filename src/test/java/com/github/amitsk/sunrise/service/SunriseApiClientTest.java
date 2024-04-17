package com.github.amitsk.sunrise.service;


import com.github.amitsk.sunrise.configuration.SunriseProperties;
import com.github.amitsk.sunrise.model.SunriseRequest;
import com.github.amitsk.sunrise.model.SunsetApiResponse;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class SunriseApiClientTest {

    @Test
    @DisplayName("Happy Path flow for Sunrise client")
    public void sunriseHappyPath() throws IOException {

        HttpUrl baseUrl;
        try (MockWebServer server = new MockWebServer()) {
            // Schedule some responses.
            MockResponse response = new MockResponse()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Cache-Control", "no-cache")
                    .setBody("{\"status\": \"OK\", \"results\": " +
                            "{\"sunrise\":\"A\", \"sunset\": \"B\"} }");

            MockResponse badResponse400 = new MockResponse()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Cache-Control", "no-cache")
                    .setResponseCode(400);

            MockResponse badResponse503 = new MockResponse()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Cache-Control", "no-cache")
                    .setResponseCode(503);

            server.enqueue(response);
            server.enqueue(badResponse400);
            server.enqueue(badResponse503);
            // Start the server.
            server.start();

            // Ask the server for its URL. You'll need this to make HTTP requests.
            baseUrl = server.url("/json/");
        }

        SunriseProperties sunriseProperties = new SunriseProperties();
        SunriseProperties.SunriseApi sunriseApi = new SunriseProperties.SunriseApi();
        sunriseApi.setUrl(baseUrl.toString());
        sunriseProperties.setSunriseApi(sunriseApi);

        WebClient webClient = WebClient.create(baseUrl.toString());
        SunriseApiClient sunriseApiClient = new SunriseApiClient(webClient);

        SunsetApiResponse.SunsetSunrise sunsetSunrise = sunriseApiClient.callApi(new SunriseRequest("2.00", "2.00")).block();
        Assertions.assertNotNull(sunsetSunrise);
        assertThat(sunsetSunrise.sunrise()).isEqualTo("A");
        assertThat(sunsetSunrise.sunset()).isEqualTo("B");

    }

}
