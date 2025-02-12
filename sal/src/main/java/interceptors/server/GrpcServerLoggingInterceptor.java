package interceptors.server;


import com.inomera.sal.util.MdcUtils;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import static com.inomera.sal.util.MdcUtils.MDC_LOG_TRACK_ID;

@Slf4j
public class GrpcServerLoggingInterceptor implements ServerInterceptor {

    @Override
    public <R, S> ServerCall.Listener<R> interceptCall(ServerCall<R, S> serverCall, Metadata metadata,
                                                       ServerCallHandler<R, S> next
    ) {
        try {
            return new WrappingListener<>(next.startCall(serverCall, metadata), MdcUtils.getOrGenerateLogTrackKey(metadata));
        } finally {
            MDC.clear();
        }
    }

    private static class WrappingListener<R> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<R> {
        private final String mdcData;

        public WrappingListener(ServerCall.Listener<R> delegate, String mdcData) {
            super(delegate);
            this.mdcData = mdcData;
        }

        @Override
        public void onMessage(R message) {
            MDC.put(MDC_LOG_TRACK_ID, mdcData);
            try {
                super.onMessage(message);
                LOG.info("onMessage : {}", message);
            } finally {
                MDC.clear();
            }
        }

        @Override
        public void onHalfClose() {
            MDC.put(MDC_LOG_TRACK_ID, mdcData);
            try {
                super.onHalfClose();
                LOG.warn("onHalfClose : {}", mdcData);
            } finally {
                MDC.clear();
            }
        }

        @Override
        public void onCancel() {
            MDC.put(MDC_LOG_TRACK_ID, mdcData);
            try {
                super.onCancel();
                LOG.warn("onCancel : {}", mdcData);
            } finally {
                MDC.clear();
            }
        }

        @Override
        public void onComplete() {
            MDC.put(MDC_LOG_TRACK_ID, mdcData);
            try {
                super.onComplete();
                LOG.info("onComplete : {}", mdcData);
            } finally {
                MDC.clear();
            }
        }

        @Override
        public void onReady() {
            MDC.put(MDC_LOG_TRACK_ID, mdcData);
            try {
                super.onReady();
                LOG.debug("onComplete : {}", mdcData);
            } finally {
                MDC.clear();
            }
        }
    }
}