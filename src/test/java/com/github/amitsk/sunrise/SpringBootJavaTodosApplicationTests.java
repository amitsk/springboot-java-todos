package com.github.amitsk.sunrise;

import com.github.amitsk.sunrise.controllers.SunriseController;
import com.github.amitsk.sunrise.service.SunriseApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@WebFluxTest(SunriseController.class)
public class SpringBootJavaTodosApplicationTests {

    @MockBean
    private SunriseApiClient sunriseApiClient;

    @Autowired

    private WebTestClient webTestClient;


	@Test
	public void contextLoads() {

	}

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
