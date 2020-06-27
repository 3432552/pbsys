package com.hzy.manager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.common.Constant;
import com.hzy.manager.common.Result;
import com.hzy.manager.common.authentication.JWTToken;
import com.hzy.manager.common.authentication.JWTUtil;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.common.exception.LoginException;
import com.hzy.manager.common.properties.FebsProperties;
import com.hzy.manager.dao.UserMapper;
import com.hzy.manager.domain.User;
import com.hzy.manager.dto.LoginUser;
import com.hzy.manager.service.UserService;
import com.hzy.manager.util.FebsUtil;
import com.hzy.manager.util.MD5Util;
import com.hzy.manager.util.PageUtils;
import com.hzy.manager.util.vcode.Captcha;
import com.hzy.manager.util.vcode.GifCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Validated
@Slf4j
public class LoginController {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private FebsProperties febsProperties;

    @RequestMapping("/mes")
    public Result mes() {
        return Result.ok();
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param code
     * @return
     * @throws LoginException
     */
    @PostMapping("/login")
    public Result userLogin(String username, String password, String code) throws LoginException {
        LoginUser loginUser = userService.findByName(username, password, code);
        String token = FebsUtil.encryptToken(JWTUtil.sign(username, MD5Util.encrypt(username, password)));
        Long expireTimes = febsProperties.getShiro().getJwtTimeOut();
        redisTemplate.opsForValue().set(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY), token, expireTimes, TimeUnit.SECONDS);
        Map<String, Object> map = new HashMap<>();
        map.put("user", loginUser);
        map.put("token", token);
        return Result.ok(map);
    }

    /**
     * 注册用户(前端要检查用户名是否存在)
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result registerUser(User user) throws BusinessException {
        userService.register(user);
        return Result.ok("注册成功!");
    }

    /**
     * 生成验证码
     *
     * @param response
     */
    @GetMapping(value = "/gifCode")
    public void getGifCode(HttpServletResponse response) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/gif");
            Captcha captcha = new GifCaptcha(Constant.WIDTH, Constant.HEIGHT, Constant.LEN);
            captcha.out(response.getOutputStream());
            redisTemplate.delete(Constant.CODE_KEY);
            redisTemplate.opsForValue().set(Constant.CODE_KEY, captcha.text().toLowerCase());
        } catch (Exception e) {
            log.error("图形验证码生成失败", e);
        }
    }
}
