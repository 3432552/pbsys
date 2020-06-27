package com.hzy.manager.common.authentication;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzy.manager.common.Constant;
import com.hzy.manager.dao.UserMapper;
import com.hzy.manager.domain.User;
import com.hzy.manager.util.FebsUtil;
import com.hzy.manager.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import javax.annotation.Resource;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 *
 * @author MrBird
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserMapper userMapper;

    //同一个bean对象
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * `
     * 授权模块，获取用户角色和权限
     *
     * @param token token
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {
        String username = JWTUtil.getUsername(token.toString());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 获取用户角色集
        /*Set<String> roleSet = userManager.getUserRoles(username);
        System.out.println("获取用户角色集=============>");
        roleSet.forEach(System.out::println);
        simpleAuthorizationInfo.setRoles(roleSet);*/

        // 获取用户权限集
        /*Set<String> permissionSet = userManager.getUserPermissions(username);
        System.out.println("获取用户权限集=============>");
        permissionSet.forEach(System.out::println);
        simpleAuthorizationInfo.setStringPermissions(permissionSet);*/
        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param authenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("=====================>9");
        // 这里的 token是从 JWTFilter 的 executeLogin 方法传递过来的，已经经过了解密
        String token = (String) authenticationToken.getCredentials();
        String username = JWTUtil.getUsername(token);
        log.info("用户名:" + username);
        if (StringUtils.isBlank(username))
            throw new AuthenticationException("token校验不通过");
        // 通过用户名查询用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));
        if (user == null) {
            throw new AuthenticationException("用户名或密码错误");
        }
        String cacheTokens = (String) redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY));
        if (!StringUtils.equals(cacheTokens, FebsUtil.encryptToken(token))) {
            throw new AuthenticationException("token校验不通过");
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
