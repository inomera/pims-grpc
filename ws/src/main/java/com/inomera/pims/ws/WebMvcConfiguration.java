package com.inomera.pims.ws;


import com.inomera.pims.ws.filter.LoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration {

    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }
}
