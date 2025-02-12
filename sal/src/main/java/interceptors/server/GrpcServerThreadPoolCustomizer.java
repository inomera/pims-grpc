package interceptors.server;

import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.springframework.grpc.server.ServerBuilderCustomizer;

import java.util.concurrent.Executors;

public class GrpcServerThreadPoolCustomizer implements ServerBuilderCustomizer<NettyServerBuilder> {

    @Override
    public void customize(NettyServerBuilder serverBuilder) {
        serverBuilder.executor(Executors.newFixedThreadPool(10));
    }
}
