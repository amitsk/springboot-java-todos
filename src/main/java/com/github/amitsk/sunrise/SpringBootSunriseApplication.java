package com.github.amitsk.sunrise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootSunriseApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringBootSunriseApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}
