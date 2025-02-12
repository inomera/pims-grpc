package interceptors.client;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcClientLoggingInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions,
                                                               Channel next
    ) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            @Override
            public void sendMessage(ReqT message) {
                LOG.info("Sending message to modules: {}, callOptions: {}", message, callOptions);
                super.sendMessage(message);
            }

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<>(responseListener) {

                    @Override
                    public void onMessage(RespT message) {
                        LOG.info("Received message from modules: {}, headers: {}", message, headers);
                        super.onMessage(message);
                    }

                }, headers);
            }

        };
    }
}
