package com.hermes.lotcenter.infrastructure.configuration;

import com.beicai.common.trace.TraceIdUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean traceIdFilter() {
        return TraceIdUtil.traceIdFilter();
    }

}
