package com.github.amitsk.sunrise.configuration;

import com.github.amitsk.sunrise.handlers.SunriseExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebExceptionHandler;

@Configuration
@EnableConfigurationProperties(SunriseProperties.class)
public class SunriseConfiguration extends DelegatingWebFluxConfiguration {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebExceptionHandler sunriseExceptionHandler() {
        return new SunriseExceptionHandler();
    }

    
}
