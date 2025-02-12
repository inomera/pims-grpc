package com.inomera.pims.msg;


import com.google.common.base.Stopwatch;
import com.inomera.pims.sal.msg.MessagingServiceGrpc;
import com.inomera.pims.sal.msg.Msg;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@GrpcService
public class MessagingGrpcService extends MessagingServiceGrpc.MessagingServiceImplBase {

    @Override
    public void sendCustomerAcknowledgeMsg(Msg.SendCustomerAcknowledgeMessageRequest request, StreamObserver<Msg.SendCustomerAcknowledgeMessageResponse> responseObserver) {
        LOG.info("request : {}", request);

        //start do stuff

        //write db
        Thread.startVirtualThread(() -> {
            Stopwatch stopwatch = Stopwatch.createStarted();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                LOG.error("InterruptedException occurred", e);
                throw new RuntimeException(e.getCause());
            }
            stopwatch.stop();
            LOG.info("Sleep finished. millis: {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        });
        //send sms via smsc (using smpp)

        //end do stuff

        Msg.SendCustomerAcknowledgeMessageResponse reply = Msg.SendCustomerAcknowledgeMessageResponse.newBuilder()
                .setMsgKey("MSG" + UUID.randomUUID().toString().replace("-", ""))
                .build();
        LOG.info("response : {}", reply);
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
