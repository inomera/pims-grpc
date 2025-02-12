package interceptors.server;

import io.grpc.*;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GrpcServerTimeoutInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {

        Context ctx = Context.current()
                .withDeadlineAfter(10, TimeUnit.SECONDS,
                        Executors.newSingleThreadScheduledExecutor()
                );

        return Contexts.interceptCall(ctx, call, headers, next);
    }
}