package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.common.exception.LoginException;
import com.hzy.manager.domain.User;
import com.hzy.manager.dto.LoginUser;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    LoginUser findByName(String username, String password, String code) throws LoginException;

    LoginUser findByUserToRegister(String userName) throws LoginException;

    void register(User user) throws BusinessException;

    List<User> getUserList(Map<String, Object> getMap);

    void addUser(User user) throws BusinessException;

    User getUserById(Long uid);

    void updateUser(User user);

    void deleteUserByIds(String[] ids);

}
