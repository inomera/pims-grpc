package com.inomera.pims.ws;


import com.inomera.pims.sal.msg.MessagingServiceGrpc;
import com.inomera.pims.sal.msg.Msg;
import com.inomera.pims.sal.pex.Pex;
import com.inomera.pims.sal.pex.ProcessExecutorServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "ws/rest/external")
public class SmsExternalRestController {

    @Qualifier("messageServiceSync")
    private final MessagingServiceGrpc.MessagingServiceBlockingV2Stub messageServiceSync;
    @Qualifier("messageServiceAsync")
    private final MessagingServiceGrpc.MessagingServiceStub messageServiceAsync;
    @Qualifier("pexServiceSync")
    private final ProcessExecutorServiceGrpc.ProcessExecutorServiceBlockingV2Stub pexServiceSync;

    @PostMapping("v10/messages/send")
    public ResponseEntity<SmsExternalResponse> send(@Validated @RequestBody SmsExternalRequest request) {
        LOG.info("request : {}", request);
        final Msg.SendCustomerAcknowledgeMessageResponse response = messageServiceSync.sendCustomerAcknowledgeMsg(Msg.SendCustomerAcknowledgeMessageRequest.newBuilder()
                .setSmsHeader(request.header())
                .setMsgTxt(request.text())
                .setReceiverMsisdn(request.msisdn())
                .setTxKey(UUID.randomUUID().toString().replace("-", ""))
                .build());
        SmsExternalResponse restResponse = new SmsExternalResponse(response.getMsgKey());
        LOG.info("response : {}", restResponse);
        return ResponseEntity.ok(restResponse);
    }

    @PostMapping("v10/messages/send/async")
    public ResponseEntity<SmsExternalResponse> sendAsync(@Validated @RequestBody SmsExternalRequest request) {
        LOG.info("request : {}", request);
        String txId = UUID.randomUUID().toString().replace("-", "");
        messageServiceAsync.sendCustomerAcknowledgeMsg(Msg.SendCustomerAcknowledgeMessageRequest.newBuilder()
                .setSmsHeader(request.header())
                .setMsgTxt(request.text())
                .setReceiverMsisdn(request.msisdn())
                .setTxKey(txId)
                .build(), new StreamObserver<>() {
            @Override
            public void onNext(Msg.SendCustomerAcknowledgeMessageResponse value) {
                LOG.info("txId : {}, response : {}", txId, value);
                //do stuff
            }

            @Override
            public void onError(Throwable t) {
                LOG.error("txId : {}, Error occurred caused!!", txId, t.getCause());
            }

            @Override
            public void onCompleted() {
                LOG.info("txId : {}, completed", txId);
            }
        });
        return ResponseEntity.ok(new SmsExternalResponse(txId));
    }

    @PostMapping("v10/messages/send/process")
    public ResponseEntity<SmsExternalResponse> sendProcess(@Validated @RequestBody SmsExternalRequest request) {
        LOG.info("request : {}", request);
        String txId = UUID.randomUUID().toString().replace("-", "");
        Pex.SendSmsProcessMessageResponse processMessageResponse = pexServiceSync.sendSms(Pex.SendSmsProcessMessageRequest.newBuilder()
                .setSmsHeader(request.header())
                .setMsgTxt(request.text())
                .setReceiverMsisdn(request.msisdn())
                .setTxKey(txId)
                .build());
        LOG.info("processMessageResponse : {}", processMessageResponse);
        return ResponseEntity.ok(new SmsExternalResponse(processMessageResponse.getMsgKey()));
    }


    public record SmsExternalRequest(String msisdn,
                                     String header,
                                     String text) implements Serializable {
    }

    public record SmsExternalResponse(String msgKey) implements Serializable {
    }
}
