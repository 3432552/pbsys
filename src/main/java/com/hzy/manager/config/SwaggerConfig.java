package com.hzy.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket buildDocket(Environment environment) {
        //设置要要显示的swagger的环境
        Profiles profiles = Profiles.of("dev", "test");
        //判断是否在设置的环境中
        boolean flag = environment.acceptsProfiles(profiles);
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder()
                .name("Authentication")
                .description("认证token")
                .modelRef(new ModelRef("String"))
                .parameterType("header")
                .required(false)
                .build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInf())
                .enable(flag)//是否关闭swagger接口文档浏览
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hzy.manager.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameters);
    }
    private ApiInfo buildApiInf() {
        return new ApiInfoBuilder()
                .title("排版系统前后端分离 RESTFUL API接口文档")
                .contact(new Contact("排版系统", "http://www.viz-cloud.com", "2261467077@qq.com"))
                .version("1.0")
                .build();
    }
}
