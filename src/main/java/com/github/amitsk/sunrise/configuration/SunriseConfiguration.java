package com.github.amitsk.sunrise.configuration;

//import com.nike.backstopper.exception.ApiException;
import com.github.amitsk.sunrise.handlers.SunriseExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebExceptionHandler;

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
    @Qualifier("sunriseWebClient")
    @Autowired
    public WebClient sunriseWebClient(SunriseProperties sunriseProperties){
        String url = sunriseProperties.getSunriseApi().getUrl();
        logger.info("Creating bean for {}", url);
        return WebClient.create(url);
    }

}
