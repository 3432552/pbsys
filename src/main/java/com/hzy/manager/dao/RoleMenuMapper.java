package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.manager.domain.RoleMenu;

public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    //关联表根据菜单Id删除角色Id
    void deleteRoleIdByMenuId(String[] mids);
}
