package com.hermes.lotcenter.infrastructure.configuration;

import com.beicai.common.annotation.ExcludeComponent;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;


@ExcludeComponent
@Configuration
public class FeignConverterConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Encoder feignEncoder(){
        final List<HttpMessageConverter<?>> springConverters = messageConverters.getObject().getConverters();
        final List<HttpMessageConverter<?>> encoderConverters
                = new ArrayList<>(springConverters.size() + 1);
        encoderConverters.addAll(springConverters);
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        encoderConverters.add(fastJsonHttpMessageConverter);
        final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(encoderConverters);
        return new SpringEncoder(()->httpMessageConverters);
    }

    @Bean
    public Decoder feignDecoder(){
        final List<HttpMessageConverter<?>> springConverters = messageConverters.getObject().getConverters();
        final List<HttpMessageConverter<?>> decoderConverters
                = new ArrayList<>(springConverters.size() + 1);
        decoderConverters.addAll(springConverters);
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        decoderConverters.add(fastJsonHttpMessageConverter);
        final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(decoderConverters);
        return new SpringDecoder(()->httpMessageConverters);
    }
}
