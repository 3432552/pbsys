package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.manager.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User> {
    //多条件查询并分页
    List<Page<User>> selectUserAndDeptPage(@Param("user") User user, Page<User> userPage);

    User selectByUid(Long uid);

    //更新登陆时间
    void updateLoginTime(User user);
}
