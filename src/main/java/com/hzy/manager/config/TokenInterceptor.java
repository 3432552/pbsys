package com.hzy.manager.config;

import com.hzy.manager.common.properties.FebsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TokenInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private FebsProperties febsProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("Authentication");
        log.info("前端请求的token:" + token);
        if (!StringUtils.isBlank(token) && redisTemplate.hasKey(token) == true) {
            //有用户操作则重置token有效时间
            redisTemplate.expire(token, febsProperties.getShiro().getTokenTimeOut(), TimeUnit.SECONDS);
            return true;
        } else {
            final String message1 = "10010";
            final String message2 = "登录认证失败,请重新登录";
            String responseJson = "{\"code\":\"" + message1 + "\",\"msg\":\"" + message2 + "\"}";
            responseMsg(response, response.getWriter(), responseJson);
            return false;
        }
    }
    //返回给客户端
    private void responseMsg(HttpServletResponse response, PrintWriter out, String resJson) {
        response.setContentType("application/json; charset=utf-8");
        out.print(resJson);
        out.flush();
        out.close();
    }
}
