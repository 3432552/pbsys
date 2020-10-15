package com.hzy.manager.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获得request和response对象
 */
public class HttpServletUtil {
    /**
     * 得到请求头token
     *
     * @return
     */
    public static String getHeaderToken() {
        return getRequest().getHeader("Authentication");
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public static HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletWebRequest) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }
}
