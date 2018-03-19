package com.github.amitsk.sunrise.configuration;

import com.github.amitsk.sunrise.handlers.SunriseExceptionHandler;
import com.github.amitsk.sunrise.service.SunriseService;
import com.jakewharton.retrofit2.adapter.reactor.ReactorCallAdapterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.server.WebExceptionHandler;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@EnableConfigurationProperties(SunriseProperties.class)
public class SunriseConfiguration extends DelegatingWebFluxConfiguration {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebExceptionHandler sunriseExceptionHandler() {
        return new SunriseExceptionHandler();
    }

    @Bean
    @Autowired
    public SunriseService sunriseService(SunriseProperties sunriseProperties) {

        String baseUrl = sunriseProperties.getSunriseApi().getUrl();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(ReactorCallAdapterFactory.createAsync())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(SunriseService.class);

    }

}
