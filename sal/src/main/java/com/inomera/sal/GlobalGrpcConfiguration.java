package com.inomera.sal;

import interceptors.client.GrpcClientLoggingInterceptor;
import interceptors.client.LogTrackKeyHeaderInterceptor;
import interceptors.server.GrpcServerLoggingInterceptor;
import interceptors.server.GrpcServerThreadPoolCustomizer;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.grpc.client.GlobalClientInterceptor;
import org.springframework.grpc.client.GrpcChannelBuilderCustomizer;
import org.springframework.grpc.server.GlobalServerInterceptor;

@Configuration
public class GlobalGrpcConfiguration {

    //TODO : Global governance config -> customize via spring GrpcClientProperties properties
    //TODO -> customize as props via yml

    //server interceptors
    @Bean
    @Order(100)
    @GlobalServerInterceptor
    public ServerInterceptor globalServerLoggingInterceptor() {
        return new GrpcServerLoggingInterceptor();
    }


    @Bean
    public GrpcServerThreadPoolCustomizer grpcServerThreadPoolCustomizer(){
        return new GrpcServerThreadPoolCustomizer();
    }


    //client interceptors
    @Bean
    @Order(100)
    @GlobalClientInterceptor
    public ClientInterceptor globalClientLoggingInterceptor() {
        return new GrpcClientLoggingInterceptor();
    }


    @Bean
    @Order(100)
    @GlobalClientInterceptor
    public ClientInterceptor logTrackKeyHeaderInterceptor() {
        return new LogTrackKeyHeaderInterceptor();
    }


    //common concerns
    @Bean
    @Order(100)
    public GrpcChannelBuilderCustomizer<NettyChannelBuilder> flowControlCustomizer() {
        return (name, builder) -> builder.flowControlWindow(1024 * 1024);
    }


    @Bean
    @Order(100)
    public <T extends ManagedChannelBuilder<T>> GrpcChannelBuilderCustomizer<T> retryChannelCustomizer() {
        return (name, builder) -> builder.disableRetry().maxRetryAttempts(3);
    }
}
