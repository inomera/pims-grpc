package com.inomera.sal.config;

import com.inomera.pims.sal.pex.ProcessExecutorServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.autoconfigure.client.GrpcClientProperties;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class PexBeanConfiguration {

    @Bean("pexServiceSync")
    public ProcessExecutorServiceGrpc.ProcessExecutorServiceBlockingV2Stub stub(GrpcChannelFactory channels, GrpcClientProperties grpcClientProperties) {
        GrpcClientProperties.ChannelConfig pexConfigs = getPexConfigs(grpcClientProperties);
        return ProcessExecutorServiceGrpc.newBlockingV2Stub(channels.createChannel(pexConfigs.getAddress()));
    }

    @Bean("pexServiceAsync")
    public ProcessExecutorServiceGrpc.ProcessExecutorServiceStub async(GrpcChannelFactory channels, GrpcClientProperties grpcClientProperties) {
        GrpcClientProperties.ChannelConfig pexConfigs = getPexConfigs(grpcClientProperties);
        return ProcessExecutorServiceGrpc.newStub(channels.createChannel(pexConfigs.getAddress()));
    }

    private GrpcClientProperties.ChannelConfig getPexConfigs(GrpcClientProperties grpcClientProperties) {
        return grpcClientProperties.getChannel("pex");
    }
}
