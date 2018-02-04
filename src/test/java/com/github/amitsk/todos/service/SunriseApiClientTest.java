package com.github.amitsk.todos.service;


import com.github.amitsk.todos.model.SunsetSunrise;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class SunriseApiClientTest {

    @Test
    @DisplayName("Happy Path flow for Sunrise client")
    public void sunriseHappyPath() throws IOException {
        MockWebServer server = new MockWebServer();
        // Schedule some responses.
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody("{\"status\": \"OK\", \"results\": " +
                        "{\"sunrise\":\"A\", \"sunset\": \"B\"} }");
        server.enqueue(response);
        // Start the server.
        server.start();

        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url("/json");
        WebClient testWebClient = WebClient.create(baseUrl.uri().toString());
        SunriseApiClient sunriseApiClient = new SunriseApiClient(testWebClient);
        SunsetSunrise sunsetSunrise = sunriseApiClient.callApi(1.00, 2.00).block();
        assertThat(sunsetSunrise.getSunrise()).isEqualTo("A");
        assertThat(sunsetSunrise.getSunset()).isEqualTo("B");

    }
}
