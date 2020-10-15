package com.hzy.manager.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hzy.manager.common.properties.FebsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class TokenConfig implements WebMvcConfigurer {
    @Autowired
    private FebsProperties febsProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor()).addPathPatterns("/**").excludePathPatterns(febsProperties.getShiro().getAnonUrl().split(StringPool.COMMA));
    }
    /**
     * 把token拦截器放在ioc容器里面
     *
     * @return
     */
    @Bean
    public TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor();
    }
}
