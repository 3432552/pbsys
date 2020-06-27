package com.hzy.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.manager.common.Constant;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.common.exception.LoginException;
import com.hzy.manager.dao.UserMapper;
import com.hzy.manager.domain.User;
import com.hzy.manager.service.UserService;
import com.hzy.manager.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public User findByName(String username, String password, String code) throws LoginException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));
        if (user == null) {
            throw new LoginException("用户或密码错误!");
        }
        if (user.getStatus().equals(Constant.LOCK)) {
            throw new LoginException("账户已被锁定!");
        }
        if (!StringUtils.equals(user.getPassword(), MD5Util.encrypt(username, password))) {
            throw new LoginException("密码输入错误!");
        }
        String cacheCode = (String) redisTemplate.opsForValue().get(Constant.CODE_KEY);
        if (!StringUtils.equals(cacheCode, code)) {
            throw new LoginException("验证码输入错误!");
        }
        return user;
    }

    @Override
    public List<User> getUserList(Map<String, Object> getMap) {
        return userMapper.selectUserAndDeptPage(getMap);
    }

    public User findByUserToRegister(String userName) throws LoginException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, userName));
        return user == null ? null : user;
    }

    @Transactional
    public void register(User user) throws BusinessException {
        user.setUserName(user.getUserName());
        user.setPassword(MD5Util.encrypt(user.getUserName(), user.getPassword()));
        user.setRealName(user.getRealName());
        Pattern pattern = Pattern.compile("^[1]\\d{10}$");
        if (pattern.matcher(user.getPhone()).matches() == false) {
            throw new BusinessException("你输入的手机号格式不正确!");
        }
        user.setPhone(user.getPhone());
        user.setSex(user.getSex());
        user.setEmail(user.getEmail());
        Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        if (p.matcher(user.getEmail()).matches() == false) {
            throw new BusinessException("你输入的邮箱格式不正确!");
        }
        user.setCreateTime(new Date());
        userMapper.insert(user);
    }

}
