package com.hermes.lotcenter.infrastructure.configuration;

import com.beicai.common.exception.RpcException;
import com.beicai.common.trace.TraceIdUtil;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
public class FeignConfig {

    @Bean
    @Profile({"dev", "pre", "test"})
    public Logger.Level feignOfflineLogger() {
        return Logger.Level.FULL;
    }

    @Bean
    @Profile("prod")
    public Logger.Level feignOnlineLogger() {
        return Logger.Level.BASIC;
    }

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return (k, response) -> {
            Request request = response.request();
            log.error("rpc request error and request : " + request + " response : " + response);
            return new RpcException("调用外部服务异常");
        };
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return (template) -> {
            String traceId = TraceIdUtil.traceId();
            template.header(TraceIdUtil.TRACE_ID, traceId);
        };
    }

}
