package com.inomera.sal.config;

import com.inomera.pims.sal.msg.MessagingServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.autoconfigure.client.GrpcClientProperties;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class MsgBeanConfiguration {

    @Bean("messageServiceSync")
    public MessagingServiceGrpc.MessagingServiceBlockingV2Stub stub(GrpcChannelFactory channels, GrpcClientProperties grpcClientProperties) {
        GrpcClientProperties.ChannelConfig msgConfigs = getMsgConfigs(grpcClientProperties);
        return MessagingServiceGrpc.newBlockingV2Stub(channels.createChannel(msgConfigs.getAddress()));
    }

    @Bean("messageServiceAsync")
    public MessagingServiceGrpc.MessagingServiceStub async(GrpcChannelFactory channels, GrpcClientProperties grpcClientProperties) {
        GrpcClientProperties.ChannelConfig msgConfigs = getMsgConfigs(grpcClientProperties);
        return MessagingServiceGrpc.newStub(channels.createChannel(msgConfigs.getAddress()));
    }

    private GrpcClientProperties.ChannelConfig getMsgConfigs(GrpcClientProperties grpcClientProperties) {
        return grpcClientProperties.getChannel("msg");
    }
}
