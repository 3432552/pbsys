package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.manager.domain.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
     List<Role> getUserRoles(String userName);
     Role getUserRole(Long uid);
}
