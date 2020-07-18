package com.hzy.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket buildDocket(Environment environment) {
        //设置要要显示的swagger的环境
        Profiles profiles = Profiles.of("dev", "test");
        //判断是否在设置的环境中
        boolean flag = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInf())
                .enable(flag)//是否关闭swagger接口文档浏览
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hzy.manager.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo buildApiInf() {
        return new ApiInfoBuilder()
                .title("排版系统前后端分离 RESTFUL API接口文档")
                .contact(new Contact("排版系统", "http://www.viz-cloud.com", "2261467077@qq.com"))
                .version("1.0")
                .build();
    }
}
