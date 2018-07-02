package com.github.amitsk.sunrise.configuration;

import brave.Tracing;
import brave.http.HttpTracing;
import brave.okhttp3.TracingInterceptor;
import brave.propagation.StrictCurrentTraceContext;
import com.github.amitsk.sunrise.handlers.SunriseExceptionHandler;
import com.github.amitsk.sunrise.service.SunriseService;
import com.jakewharton.retrofit2.adapter.reactor.ReactorCallAdapterFactory;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
import zipkin2.reporter.Reporter;

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
        Tracing tracing = Tracing.newBuilder()
                .currentTraceContext(new StrictCurrentTraceContext())
                .spanReporter(Reporter.CONSOLE)
                .build();
        HttpTracing httpTracing = HttpTracing.create(tracing);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


        httpClient.dispatcher(new Dispatcher(
                httpTracing.tracing().currentTraceContext()
                        .executorService(new Dispatcher().executorService())
        )).addNetworkInterceptor(TracingInterceptor.create(httpTracing))
                .build();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        String baseUrl = sunriseProperties.getSunriseApi().getUrl();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(ReactorCallAdapterFactory.createAsync())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(SunriseService.class);

    }

}
