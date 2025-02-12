package com.inomera.pims.ws;

import com.inomera.sal.GlobalGrpcConfiguration;
import com.inomera.sal.config.MsgBeanConfiguration;
import com.inomera.sal.config.PexBeanConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({GlobalGrpcConfiguration.class, PexBeanConfiguration.class, MsgBeanConfiguration.class})
@SpringBootApplication
public class WsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsApplication.class, args);
    }

}
