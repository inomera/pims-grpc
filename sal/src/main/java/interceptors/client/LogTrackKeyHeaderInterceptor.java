package interceptors.client;

import com.inomera.sal.util.MdcUtils;
import io.grpc.*;

import static com.inomera.sal.util.MdcUtils.MDC_LOG_TRACK_KEY;

public class LogTrackKeyHeaderInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            public void start(ClientCall.Listener<RespT> responseListener, io.grpc.Metadata headers) {
                headers.put(MDC_LOG_TRACK_KEY, MdcUtils.getOrGenerateLogTrackKey());
                super.start(responseListener, headers);
            }
        };
    }
}
