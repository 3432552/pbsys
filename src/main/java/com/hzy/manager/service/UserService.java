package com.hzy.manager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.manager.common.exception.BusinessException;
import com.hzy.manager.common.exception.LoginException;
import com.hzy.manager.domain.User;
import com.hzy.manager.vo.BroadcastUserVo;
import com.hzy.manager.vo.LoginUser;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    LoginUser findByName(String username, String password) throws LoginException;

    LoginUser findByUserToRegister(String userName) throws LoginException;

    void register(User user) throws BusinessException;

    List<Page<User>> getUserAndDeptPage(User user, Page<User> userPage);

    void addUser(User user) throws BusinessException;

    User getUserById(Long uid);

    void updateUser(User user);

    void deleteUserByIds(String[] ids);

    List<BroadcastUserVo> selectAllBroadcastUser();

    List<BroadcastUserVo> getBlurBroadcastUser(String realName);

    //修改密码
    int updatePwd(User user) throws BusinessException;

}
