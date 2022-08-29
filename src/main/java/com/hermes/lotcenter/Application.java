package com.hermes.lotcenter;

import com.beicai.common.annotation.ExcludeComponent;
import org.mybatis.spring.annotation.MapperScan;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@ServletComponentScan
@EnableScheduling
@EnableFeignClients(basePackageClasses = Application.class, basePackages = {"com.hermes.lotcenter"})
@ComponentScan(basePackages = {"com.hermes.lotcenter"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {ExcludeComponent.class})})
@MapperScan({"com.hermes.lotcenter.repository"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
