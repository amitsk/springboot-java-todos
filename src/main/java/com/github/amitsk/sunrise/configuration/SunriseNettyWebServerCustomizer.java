package com.github.amitsk.sunrise.configuration;

import io.netty.channel.EventLoopGroup;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import reactor.ipc.netty.http.server.HttpServer;
import reactor.ipc.netty.http.server.HttpServerOptions;

@Component
public class SunriseNettyWebServerCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    @Override
    public void customize(NettyReactiveWebServerFactory nettyFactory) {
        nettyFactory.addServerCustomizers(new EventLoopNettyCustomizer());
    }

    class EventLoopNettyCustomizer implements NettyServerCustomizer {


        @Override
        public void customize(HttpServerOptions.Builder builder) {
//            builder.eventLoopGroup(EventLoopGroup)

        }
    }
}
