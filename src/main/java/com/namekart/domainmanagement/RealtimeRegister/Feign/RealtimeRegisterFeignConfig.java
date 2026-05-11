package com.namekart.domainmanagement.RealtimeRegister.Feign;

import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class RealtimeRegisterFeignConfig {

    @Value("${realtimeregister.api.key}")
    private String apiKey;

    @Bean
    public ErrorDecoder rrErrorDecoder() {
        return new RealtimeRegisterErrorDecoder();
    }

    @Bean
    public Retryer rrRetryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    public RequestInterceptor rrAuthInterceptor() {
        return requestTemplate -> requestTemplate.header("Authorization", "ApiKey " + apiKey);
    }
}
