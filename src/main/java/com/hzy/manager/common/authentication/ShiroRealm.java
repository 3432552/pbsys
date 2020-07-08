package com.hzy.manager.common.authentication;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzy.manager.common.Constant;
import com.hzy.manager.dao.LoginUserMapper;
import com.hzy.manager.dao.MenuMapper;
import com.hzy.manager.dao.RoleMapper;
import com.hzy.manager.dao.UserMapper;
import com.hzy.manager.domain.Menu;
import com.hzy.manager.domain.Role;
import com.hzy.manager.domain.User;
import com.hzy.manager.dto.LoginUser;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 *
 * @author MrBird
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private LoginUserMapper loginUserMapper;

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
        log.info("进入授权功能====================================================》");
        String username = JWTUtil.getUsername(token.toString());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 获取用户角色集
        List<Role> roleList = roleMapper.getUserRoles(username);
        Set<String> roleSet = roleList.stream().map(Role::getRoleName).collect(Collectors.toSet());
        log.info("获取用户角色集=============>" + roleSet);
        simpleAuthorizationInfo.setRoles(roleSet);

        // 获取用户权限集
        List<Menu> permissionList = menuMapper.getUserPermissions(username);
        Set<String> permissionSet = permissionList.stream().map(Menu::getPerms).collect(Collectors.toSet());
        log.info("获取用户权限集=============>" + permissionSet);
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
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
        LoginUser loginUser = loginUserMapper.findByUserName(username);
        if (loginUser == null) {
            throw new AuthenticationException("用户名或密码错误");
        }
       /* String cacheTokens = (String) redisTemplate.opsForValue().get(MD5Util.encrypt(Constant.TOKEN_CACHE_KEY));
        if (!StringUtils.equals(cacheTokens, FebsUtil.encryptToken(token))) {
            throw new AuthenticationException("token校验不通过");
        }*/
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
