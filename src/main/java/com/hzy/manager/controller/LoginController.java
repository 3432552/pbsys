package com.hzy.manager.controller;

import com.hzy.manager.common.Constant;
import com.hzy.manager.common.Result;
import com.hzy.manager.common.authentication.JWTUtil;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.common.exception.LoginException;
import com.hzy.manager.common.properties.FebsProperties;
import com.hzy.manager.domain.User;
import com.hzy.manager.vo.LoginUser;
import com.hzy.manager.service.UserService;
import com.hzy.manager.util.FebsUtil;
import com.hzy.manager.util.MD5Util;
import com.hzy.manager.util.vcode.Captcha;
import com.hzy.manager.util.vcode.GifCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@Api(tags = "登录控制类")
public class LoginController {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private FebsProperties febsProperties;

    /**
     * 用户登录
     *
     * @return
     * @throws LoginException
     */
    @ApiOperation(value = "用户登录")
    @ApiImplicitParam(name = "LoginUser", value = "LoginUser实体", required = true, dataType = "LoginUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @PostMapping("/login")
    public Result userLogin(@RequestBody LoginUser loginUser) throws LoginException {
        LoginUser loginUser1 = userService.findByName(loginUser.getUserName(), loginUser.getPassword(), loginUser.getCode());
        String token = FebsUtil.encryptToken(JWTUtil.sign(loginUser.getUserName(), MD5Util.encrypt(loginUser.getUserName(), loginUser.getPassword())));
        Long expireTimes = febsProperties.getShiro().getJwtTimeOut();
        redisTemplate.opsForValue().set(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY), token, expireTimes, TimeUnit.SECONDS);
        Map<String, Object> map = new HashMap<>();
        map.put("user", loginUser1);
        map.put("token", token);
        map.put("expirestime", expireTimes);
        return Result.ok(map);
    }

    /**
     * 注册用户(前端要检查用户名是否存在,带新增部门)
     * 必传参数:userName,password,realName,phone,sex,email,deptId
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "用户注册")
    @ApiImplicitParam(name = "User", value = "User实体", required = true, dataType = "User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码)", required = true),
            @ApiImplicitParam(name = "realName", value = "用户真实名字", required = true),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true),
            @ApiImplicitParam(name = "sex(String: 0:男 1:女)", value = "性别", required = true),
            @ApiImplicitParam(name = "email", value = "电子邮件", required = true),
            @ApiImplicitParam(name = "deptId(Long)", value = "部门id", required = true),
            @ApiImplicitParam(name = "describle", value = "个性签名", required = true)
    })
    @PostMapping("/register")
    public Result registerUser(@RequestBody User user) throws BusinessException {
        userService.register(user);
        return Result.ok("注册成功");
    }

    /**
     * 生成验证码
     *
     * @param response
     */
    @ApiOperation(value = "生成验证码")
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


    /**
     * 用户登录退出
     *
     * @return
     */
    @ApiOperation(value = "退出登录")
    @PostMapping("/logOut")
    public Result loginOutMes() {
        //把token信息删除
        boolean a = redisTemplate.delete(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY));
        if (a == true) {
            return Result.ok("退出登录成功");
        } else {
            return Result.ok("退出登录失败");
        }
    }
}
