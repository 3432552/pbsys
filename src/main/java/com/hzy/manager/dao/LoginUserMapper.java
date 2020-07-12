package com.hzy.manager.dao;

import com.hzy.manager.vo.LoginUser;

public interface LoginUserMapper {
    LoginUser findByUserName(String userName);
}
