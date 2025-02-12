package com.inomera.pims.pex;

import com.inomera.sal.GlobalGrpcConfiguration;
import com.inomera.sal.config.MsgBeanConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({GlobalGrpcConfiguration.class, MsgBeanConfiguration.class})
public class PexApplication {

    public static void main(String[] args) {
        SpringApplication.run(PexApplication.class, args);
    }

}
