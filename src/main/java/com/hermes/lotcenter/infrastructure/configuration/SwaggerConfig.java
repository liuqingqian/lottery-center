package com.hermes.lotcenter.infrastructure.configuration;

import com.beicai.common.constants.CommonConstants;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@Profile(value = {"test", "dev"})
@EnableSwagger2
public class SwaggerConfig {

	public static final String SWAGGER_SCAN_PACKAGE = "com.hermes.lotcenter.application";
	public static final String APP_PACKAGE = ".app";
	public static final String WEB_PACKAGE = ".web";

    @Bean
    public Docket createAppRestApi() {
        ParameterBuilder headerPar = new ParameterBuilder();
        List<Parameter> pars = Lists.newArrayListWithCapacity(1);
        headerPar.name(CommonConstants.USER_HEADER).description("用户信息")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build();
        pars.add(headerPar.build());
        return new Docket(SWAGGER_2)
        		.groupName("APP接口")
                .apiInfo(apiInfo())
                .select()
				.apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_PACKAGE + APP_PACKAGE))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(pars);
    }

    @Bean
    public Docket createWebRestApi() {
        ParameterBuilder headerPar = new ParameterBuilder();
        List<Parameter> pars = Lists.newArrayListWithCapacity(1);
        headerPar.name(CommonConstants.USER_HEADER).description("用户信息")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build();
        pars.add(headerPar.build());
        return new Docket(SWAGGER_2)
        		.groupName("WEB接口")
                .apiInfo(apiInfo())
                .select()
				.apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_PACKAGE + WEB_PACKAGE))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("")
                .description("")
                .termsOfServiceUrl("")
                .version("1")
                .build();
    }

}
