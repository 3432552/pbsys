package com.hzy.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.manager.domain.RoleMenu;
import org.apache.ibatis.annotations.Param;

public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    //关联表根据菜单Id删除角色Id
    void deleteRoleIdByMenuId(String[] mids);
    //角色菜单中间表删除当前播控用户角色对应的修改工作日志的菜单
    void deleteRoleMenuById(@Param("rId") Long rId,@Param("mId") Long mId);
}
