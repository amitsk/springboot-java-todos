package com.github.amitsk.sunrise.handlers;

import com.github.amitsk.sunrise.errors.SunriseApiError;
import com.nike.backstopper.exception.ApiException;
import com.nike.backstopper.model.DefaultErrorContractDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SunriseExceptionHandler implements WebExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return handle(ex).flatMap(serverResponse ->
                serverResponse.writeTo(exchange, new
                        HandlerStrategiesResponseContext(HandlerStrategies.withDefaults()))
        ).flatMap(aVoid -> Mono.empty());
    }

    private Mono<ServerResponse> handle(Throwable throwable) {
        logger.error("Throwable throwable = {}", throwable.toString());
        return createResponse(throwable);
    }

    private Mono<ServerResponse> createResponse(Throwable throwable) {
        if (throwable instanceof ApiException) {
            ApiException apiException = (ApiException) throwable;
            HttpStatus httpStatus = apiException.getApiErrors().stream()
                    .findFirst().map(x -> HttpStatus.valueOf(x.getHttpStatusCode()))
                    .orElse(HttpStatus.INTERNAL_SERVER_ERROR);

            DefaultErrorContractDTO errorDto = new DefaultErrorContractDTO(UUID.randomUUID().toString(), apiException.getApiErrors());
            logger.error("Error handling request error_id={}, message={}, status={}", errorDto.error_id, apiException.getMessage(), httpStatus);
            if (HttpStatus.INTERNAL_SERVER_ERROR.equals(httpStatus)) {
                logger.error(" Internal Server Error", apiException);
            }
            return ServerResponse
                    .status(httpStatus).header("error_id", errorDto.error_id)
                    .syncBody(errorDto);
        }

        logger.error("Switching to Default Error handling for request", throwable);
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR).header("error_id", UUID.randomUUID().toString())
                .syncBody(new DefaultErrorContractDTO(UUID.randomUUID().toString(),
                        Collections.singletonList(SunriseApiError.GENERIC_SERVICE_ERROR)));
    }

    private class HandlerStrategiesResponseContext implements ServerResponse.Context {
        private final HandlerStrategies handlerStrategies;

        private HandlerStrategiesResponseContext(HandlerStrategies handlerStrategies) {
            this.handlerStrategies = handlerStrategies;
        }

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return this.handlerStrategies.messageWriters();
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return this.handlerStrategies.viewResolvers();
        }
    }
}
