package com.inomera.pims.pex;


import com.google.common.base.Stopwatch;
import com.inomera.pims.sal.msg.MessagingServiceGrpc;
import com.inomera.pims.sal.msg.Msg;
import com.inomera.pims.sal.pex.Pex;
import com.inomera.pims.sal.pex.ProcessExecutorServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.grpc.server.service.GrpcService;

import java.util.concurrent.TimeUnit;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ProcessExecutorGrpcService extends ProcessExecutorServiceGrpc.ProcessExecutorServiceImplBase {

    @Qualifier("messageServiceSync")
    private final MessagingServiceGrpc.MessagingServiceBlockingV2Stub messageServiceSync;

    @Override
    public void sendSms(Pex.SendSmsProcessMessageRequest request, StreamObserver<Pex.SendSmsProcessMessageResponse> responseObserver) {
        LOG.info("request : {}", request);

        //start do stuff
        sleep("DB Sleep", 25L);
        //catalog service
        sleep("Catalog Sleep", 25L);
        //customer service
        sleep("Customer Sleep", 50L);
        //charging service
        sleep("Charging Sleep", 100L);
        //messaging service

        final Msg.SendCustomerAcknowledgeMessageResponse response = messageServiceSync.sendCustomerAcknowledgeMsg(Msg.SendCustomerAcknowledgeMessageRequest.newBuilder()
                .setSmsHeader(request.getSmsHeader())
                .setMsgTxt(request.getMsgTxt())
                .setReceiverMsisdn(request.getReceiverMsisdn())
                .setTxKey(request.getTxKey())
                .build());
        //end do stuff

        Pex.SendSmsProcessMessageResponse reply = Pex.SendSmsProcessMessageResponse.newBuilder()
                .setMsgKey(response.getMsgKey())
                .build();
        LOG.info("response : {}", reply);
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    private void sleep(String logPrefix, long sleepTime) {
        Thread.startVirtualThread(() -> {
            Stopwatch stopwatch = Stopwatch.createStarted();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                LOG.error("{} InterruptedException occurred", logPrefix, e);
                throw new RuntimeException(e.getCause());
            }
            stopwatch.stop();
            LOG.info(" {} Duration millis: {}", logPrefix, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        });
    }

}
