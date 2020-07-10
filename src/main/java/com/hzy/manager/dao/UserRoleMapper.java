package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.manager.domain.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole> {
    //根据用户id删除角色Id
    void deleteRoleIdByUid(String uid);
}
