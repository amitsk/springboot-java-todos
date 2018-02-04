package com.github.amitsk.todos.configuration;

//import com.nike.backstopper.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SunriseExceptionHandler implements WebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return handle(ex).flatMap(serverResponse ->
          serverResponse.writeTo(exchange, new
                        HandlerStrategiesResponseContext(HandlerStrategies.withDefaults()))
        ).flatMap(aVoid -> Mono.empty());
    }

    // TODO FIXME Yo!
    private Mono<ServerResponse> handle(Throwable throwable) {
       return  createResponse(HttpStatus.INTERNAL_SERVER_ERROR, throwable.toString());
    }

    //TODO _ fix
    private  Mono<ServerResponse> createResponse(HttpStatus httpStatus, String code) {
        return  ServerResponse
                .status(httpStatus).header("Error", code).build();
               // .syncBody(TODO_NOT_FOUND_ERROR);
    }


    private class HandlerStrategiesResponseContext implements ServerResponse.Context {
        private final HandlerStrategies handlerStrategies;

        private HandlerStrategiesResponseContext(HandlerStrategies handlerStrategies) {
            this.handlerStrategies = handlerStrategies;
        }

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return null;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return null;
        }
    }
}
