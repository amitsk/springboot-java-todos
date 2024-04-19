package com.github.amitsk.sunrise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class SpringBootSunriseApplication {

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        SpringApplication app = new SpringApplication(SpringBootSunriseApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}
