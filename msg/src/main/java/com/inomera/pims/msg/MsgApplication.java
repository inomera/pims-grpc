package com.inomera.pims.msg;

import com.inomera.sal.GlobalGrpcConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({GlobalGrpcConfiguration.class})
@SpringBootApplication
public class MsgApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsgApplication.class, args);
    }


}
