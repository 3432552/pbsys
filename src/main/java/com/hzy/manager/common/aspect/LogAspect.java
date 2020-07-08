package com.hzy.manager.common.aspect;

import com.hzy.manager.common.Constant;
import com.hzy.manager.common.annotation.hasPermission;
import com.hzy.manager.dao.MenuMapper;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.dto.LoginUser;
import com.hzy.manager.util.HttpContextUtils;
import com.hzy.manager.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.util.WebUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Autowired
    private MenuMapper menuMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private HttpServletResponse response;

    @Pointcut("@annotation(com.hzy.manager.common.annotation.hasPermission)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        result = point.proceed();
        //获取自定义注解的值
        hasPermission logValue = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(hasPermission.class);
        log.info("此时访问接口的权限:" + logValue.value());
        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.USER_CACHE));
        List<Menu> menuList = menuMapper.getUserPermissions(loginUser.getUserName());
        boolean flag = false;
        for (Menu m : menuList
        ) {
            if (m.getPerms().equals(logValue.value())) {
                flag = true;
            }
        }
        //没权限
        if (!flag) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            final String message1 = "401";
            final String message2 = "你没有此权限!";
            try (PrintWriter out = response.getWriter()) {
                StringBuilder stringBuilder = new StringBuilder();
                String responseJson1 = "{\"code\":\"" + message1 + "\"}";
                String responseJson2 = "{\"msg\":\"" + message2 + "\"}";
                stringBuilder.append(responseJson1 + ",");
                stringBuilder.append(responseJson2);
                out.print(stringBuilder);
            } catch (IOException e) {
                log.error("PrintWriter error：", e);
            }
        }
        return result;
    }
}
