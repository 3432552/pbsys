package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.manager.domain.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    //根据用户名查找用户的角色(其实一个用户对应一个角色，主要通过角色去授权菜单权限)
    List<Role> getUserRoles(String userName);

    //根据用户id查找用户角色
    Role getUserRole(Long uid);

    //根据角色id查找角色的所拥有的所有菜单
    List<Role> selMenuIds(Long roleId);
}
