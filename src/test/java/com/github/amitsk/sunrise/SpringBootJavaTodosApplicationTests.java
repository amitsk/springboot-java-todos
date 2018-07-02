package com.github.amitsk.sunrise;

import com.github.amitsk.sunrise.controllers.SunriseController;
import com.github.amitsk.sunrise.service.SunriseApiClient;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@WebFluxTest(SunriseController.class)
@Import({MetricsAutoConfiguration.class, CompositeMeterRegistryAutoConfiguration.class})
public class SpringBootJavaTodosApplicationTests {

    @MockBean
    private SunriseApiClient sunriseApiClient;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MeterRegistry registry;

    @Test
    public void testHappyPath() {

        webTestClient.get()
                .uri("/sunrise/10.0/12.0")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("");

    }

}
