package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.manager.domain.User;
import com.hzy.manager.dto.LoginUser;

import java.util.List;
import java.util.Map;

public interface LoginUserMapper {
    LoginUser findByUserName(String userName);
}
