package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.common.exception.LoginException;
import com.hzy.manager.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    User findByName(String username, String password, String code) throws LoginException;

    User findByUserToRegister(String userName) throws LoginException;

    void register(User user) throws BusinessException;

    List<User> getUserList(Map<String, Object> getMap);

}
