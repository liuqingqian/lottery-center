package com.hermes.lotcenter.infrastructure.configuration;

import com.beicai.common.exception.RpcException;
import com.hermes.lotcenter.domain.LotteryConfig;
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
//            String traceId = TraceIdUtil.traceId();
//            template.header(TraceIdUtil.TRACE_ID, traceId);
            template.header("Host", LotteryConfig.HOST);
            template.header("Origin", LotteryConfig.ORIGIN);
            template.header("Accept-Encoding", "gzip, deflate");
            template.header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
            template.header("Pragma", "no-cache");
            template.header("Cache-Control", "no-cache");
            template.header("Proxy-Connection", "keep-alive");
            template.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");
        };
    }

}
